import React, { useState, useEffect } from 'react';
import { Home, PlusSquare, Settings as SettingsIcon } from 'lucide-react';
import Navbar from './components/Layout/Navbar';
import Dashboard from './components/Dashboard/Dashboard';
import RapidQuoteForm from './components/Quotes/RapidQuoteForm';
import QuoteView from './components/Quotes/QuoteView';
import Settings from './components/Dashboard/Settings';

function App() {
  const [view, setView] = useState('dashboard'); // 'dashboard', 'form', 'view', 'settings'
  const [selectedQuote, setSelectedQuote] = useState(null);
  const [quotes, setQuotes] = useState(() => {
    const saved = localStorage.getItem('freonflow_quotes');
    return saved ? JSON.parse(saved) : [
      { id: 101, client_name: 'John Smith', date: '2023-10-24', total_amount: 425.00, status: 'sent', items: [] },
      { id: 102, client_name: 'Jane Doe', date: '2023-10-23', total_amount: 1250.00, status: 'approved', items: [] },
    ];
  });

  // Local Storage persistence
  useEffect(() => {
    localStorage.setItem('freonflow_quotes', JSON.stringify(quotes));
  }, [quotes]);

  // Online Sync Mock
  useEffect(() => {
    const handleOnline = () => {
      console.log("Back online! Syncing data to Supabase...");
      // Logic to find 'draft' or unsynced quotes and push to Supabase
    };
    window.addEventListener('online', handleOnline);
    return () => window.removeEventListener('online', handleOnline);
  }, [quotes]);

  const handleCreateQuote = () => {
    setView('form');
    window.history.pushState({ view: 'form' }, '');
  };

  const handleSaveQuote = (quoteData) => {
    const newQuote = {
      id: Date.now(),
      client_name: quoteData.newClient.full_name || 'Quick Client',
      address: quoteData.newClient.address,
      date: new Date().toLocaleDateString(),
      total_amount: quoteData.total,
      status: 'draft',
      items: quoteData.items
    };
    setQuotes([newQuote, ...quotes]);
    setSelectedQuote(newQuote);
    setView('view');
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
          />
        )}

        {view === 'view' && selectedQuote && (
          <QuoteView
            quote={selectedQuote}
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
