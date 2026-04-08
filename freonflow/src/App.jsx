import React, { useState, useEffect } from 'react';
import Navbar from './components/Layout/Navbar';
import Dashboard from './components/Dashboard/Dashboard';
import RapidQuoteForm from './components/Quotes/RapidQuoteForm';
import QuoteView from './components/Quotes/QuoteView';

function App() {
  const [view, setView] = useState('dashboard'); // 'dashboard', 'form', 'view'
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
  };

  const goBack = () => {
    setView('dashboard');
    setSelectedQuote(null);
  };

  return (
    <div className="max-w-md mx-auto min-h-screen bg-white shadow-xl relative">
      {view !== 'view' && <Navbar onLogoClick={goBack} />}

      <main>
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
      </main>
    </div>
  );
}

export default App;
