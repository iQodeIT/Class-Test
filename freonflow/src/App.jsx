import React, { useState, useEffect } from 'react';
import { Home, PlusSquare, Settings as SettingsIcon } from 'lucide-react';
import { supabase } from './lib/supabase';
import Navbar from './components/Layout/Navbar';
import Dashboard from './components/Dashboard/Dashboard';
import RapidQuoteForm from './components/Quotes/RapidQuoteForm';
import QuoteView from './components/Quotes/QuoteView';
import Settings from './components/Dashboard/Settings';

function App() {
  const [view, setView] = useState('dashboard'); // 'dashboard', 'form', 'view', 'settings'
  const [selectedQuote, setSelectedQuote] = useState(null);
  const [profile, setProfile] = useState(null);
  const [clients, setClients] = useState([]);
  const [quotes, setQuotes] = useState(() => {
    const saved = localStorage.getItem('freonflow_quotes');
    return saved ? JSON.parse(saved) : [];
  });

  // Fetch data on mount
  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const { data: { user } } = await supabase.auth.getUser();
    if (user) {
      const { data: userData } = await supabase.from('users').select('*').eq('id', user.id).single();
      if (userData) setProfile(userData);
    }

    const { data: clientsData } = await supabase.from('clients').select('*');
    if (clientsData) setClients(clientsData);

    const { data: quotesData } = await supabase
      .from('quotes')
      .select(`
        *,
        clients (full_name),
        quote_items (*)
      `)
      .order('created_at', { ascending: false });

    if (quotesData) {
      const formattedQuotes = quotesData.map(q => ({
        ...q,
        client_name: q.clients?.full_name || 'Unknown',
        items: q.quote_items
      }));
      setQuotes(formattedQuotes);
    }
  };

  // Local Storage persistence as fallback
  useEffect(() => {
    if (quotes.length > 0) {
      localStorage.setItem('freonflow_quotes', JSON.stringify(quotes));
    }
  }, [quotes]);

  // Online Sync Logic
  useEffect(() => {
    const handleOnline = async () => {
      console.log("Back online! Syncing pending quotes to Supabase...");
      const pending = JSON.parse(localStorage.getItem('freonflow_pending_quotes') || '[]');
      if (pending.length === 0) return;

      const stillPending = [];
      for (const quoteData of pending) {
        try {
          const success = await handleSaveQuote(quoteData, true);
          if (!success) stillPending.push(quoteData);
        } catch (e) {
          stillPending.push(quoteData);
        }
      }
      localStorage.setItem('freonflow_pending_quotes', JSON.stringify(stillPending));
    };
    window.addEventListener('online', handleOnline);
    return () => window.removeEventListener('online', handleOnline);
  }, [quotes]);

  const handleCreateQuote = () => {
    setView('form');
    window.history.pushState({ view: 'form' }, '');
  };

  const handleSaveQuote = async (quoteData, isSyncing = false) => {
    try {
      if (!navigator.onLine && !isSyncing) {
        const pending = JSON.parse(localStorage.getItem('freonflow_pending_quotes') || '[]');
        localStorage.setItem('freonflow_pending_quotes', JSON.stringify([...pending, quoteData]));

        // Optimistic UI update
        const offlineQuote = {
          id: Date.now(),
          client_name: quoteData.newClient?.full_name || clients.find(c => c.id === quoteData.clientId)?.full_name || 'Client',
          items: quoteData.items,
          total_amount: quoteData.total,
          status: 'draft',
          date: new Date().toLocaleDateString(),
          is_offline: true
        };
        setQuotes([offlineQuote, ...quotes]);
        setSelectedQuote(offlineQuote);
        setView('view');
        return true;
      }

      const { data: { user } } = await supabase.auth.getUser();
      if (!user) throw new Error("User session not found. Please log in.");

      let finalClientId = quoteData.clientId;

      // 1. Handle New Client creation
      if (!finalClientId && quoteData.newClient.full_name) {
        const { data: client, error: clientError } = await supabase
          .from('clients')
          .insert({
            user_id: user.id,
            full_name: quoteData.newClient.full_name,
            address: quoteData.newClient.address
          })
          .select()
          .single();

        if (clientError) throw clientError;
        finalClientId = client.id;
        setClients([client, ...clients]);
      }

      if (!finalClientId) throw new Error("A client must be selected or created.");

      // 2. Insert Quote
      const { data: quote, error: quoteError } = await supabase
        .from('quotes')
        .insert({
          user_id: user.id,
          client_id: finalClientId,
          status: 'draft',
          total_amount: quoteData.total,
          notes: "" // Using provided schema field
        })
        .select()
        .single();

      if (quoteError) throw quoteError;

      // 3. Insert Quote Items
      const itemsToInsert = quoteData.items.map(item => ({
        quote_id: quote.id,
        description: item.description,
        quantity: item.quantity,
        unit_price: item.unit_price
      }));

      const { error: itemsError } = await supabase
        .from('quote_items')
        .insert(itemsToInsert);

      if (itemsError) throw itemsError;

      // 4. Update UI State
      const savedQuote = {
        ...quote,
        client_name: quoteData.newClient.full_name || clients.find(c => c.id === finalClientId)?.full_name || 'Client',
        items: quoteData.items,
        date: new Date(quote.created_at).toLocaleDateString()
      };

      setQuotes([savedQuote, ...quotes]);
      setSelectedQuote(savedQuote);
      setView('view');
      return true;
    } catch (error) {
      console.error("Supabase Save Error:", error.message);
      alert("Error saving quote: " + error.message);
      return false;
    }
  };

  const handleSelectQuote = (quote) => {
    setSelectedQuote(quote);
    setView('view');
    window.history.pushState({ view: 'view' }, '');
  };

  const goBack = () => {
    if (view !== 'dashboard') {
      setView('dashboard');
      setSelectedQuote(null);
    }
  };

  useEffect(() => {
    const handlePopState = (event) => {
      if (event.state && event.state.view) {
        setView(event.state.view);
      } else {
        setView('dashboard');
        setSelectedQuote(null);
      }
    };
    window.addEventListener('popstate', handlePopState);
    return () => window.removeEventListener('popstate', handlePopState);
  }, []);

  return (
    <div className="max-w-md mx-auto min-h-screen bg-white dark:bg-zinc-950 shadow-xl relative pb-20">
      {view !== 'view' && <Navbar onLogoClick={goBack} />}

      <main className="animate-in fade-in slide-in-from-bottom-2 duration-300">
        {view === 'dashboard' && (
          <Dashboard
            onCreateQuote={handleCreateQuote}
            onSelectQuote={handleSelectQuote}
            quotes={quotes}
          />
        )}

        {view === 'form' && (
          <RapidQuoteForm
            onSave={handleSaveQuote}
            onBack={goBack}
            initialClients={clients}
          />
        )}

        {view === 'view' && selectedQuote && (
          <QuoteView
            quote={selectedQuote}
            profile={profile}
            onBack={goBack}
          />
        )}

        {view === 'settings' && (
          <Settings onBack={goBack} />
        )}
      </main>

      {/* Bottom Tabs */}
      {view !== 'view' && view !== 'form' && (
        <nav className="bottom-nav flex justify-around items-center h-16">
          <button
            onClick={() => setView('dashboard')}
            className={`flex flex-col items-center gap-1 ${view === 'dashboard' ? 'text-hvac-blue' : 'text-zinc-400'}`}
          >
            <Home size={24} />
            <span className="text-[10px] font-bold">Home</span>
          </button>
          <button
            onClick={() => setView('form')}
            className={`flex flex-col items-center gap-1 ${view === 'form' ? 'text-hvac-blue' : 'text-zinc-400'}`}
          >
            <PlusSquare size={24} />
            <span className="text-[10px] font-bold">New Quote</span>
          </button>
          <button
            onClick={() => {
              setView('settings');
              window.history.pushState({ view: 'settings' }, '');
            }}
            className={`flex flex-col items-center gap-1 ${view === 'settings' ? 'text-hvac-blue' : 'text-zinc-400'}`}
          >
            <SettingsIcon size={24} />
            <span className="text-[10px] font-bold">Settings</span>
          </button>
        </nav>
      )}
    </div>
  );
}

export default App;
