import React from 'react';
import { Plus, ChevronRight, FileText, Clock, CheckCircle, RefreshCcw } from 'lucide-react';

const RecentQuoteCard = ({ quote, onClick }) => {
  const getStatusIcon = (status) => {
    switch (status) {
      case 'approved': return <CheckCircle size={16} className="text-green-600" />;
      case 'sent': return <Clock size={16} className="text-blue-600" />;
      default: return <FileText size={16} className="text-gray-600" />;
    }
  };

  return (
    <div
      className="card mb-4 active:scale-[0.98] transition-transform cursor-pointer"
      onClick={() => onClick(quote)}
    >
      <div className="flex justify-between items-start mb-2">
        <div>
          <h3 className="font-bold text-xl">{quote.client_name}</h3>
          <p className="text-zinc-500 font-medium">{quote.date}</p>
        </div>
        <div className="text-right">
          <p className="font-black text-2xl">${quote.total_amount.toFixed(2)}</p>
          <div className="flex items-center justify-end gap-1 mt-1">
            {getStatusIcon(quote.status)}
            <span className="text-xs font-bold uppercase tracking-wider">{quote.status}</span>
          </div>
        </div>
      </div>
      <div className="flex items-center justify-end text-hvac-blue font-bold">
        <span>View Details</span>
        <ChevronRight size={20} />
      </div>
    </div>
  );
};

const Dashboard = ({ onCreateQuote, onSelectQuote, quotes }) => {
  const [refreshing, setRefreshing] = React.useState(false);

  const handleRefresh = () => {
    setRefreshing(true);
    setTimeout(() => setRefreshing(false), 1500);
  };

  return (
    <div className="p-4 pb-32">
      {/* Pull to Refresh Mock */}
      <div
        className={`flex items-center justify-center overflow-hidden transition-all duration-300 ${refreshing ? 'h-16 opacity-100' : 'h-0 opacity-0'}`}
        onClick={handleRefresh}
      >
        <RefreshCcw className="animate-spin text-hvac-blue" size={24} />
      </div>

      <div className="mb-8">
        <h1 className="text-3xl font-black mb-2">Welcome Back!</h1>
        <p className="text-zinc-500 font-medium italic">"Get in, get paid, get home."</p>
      </div>

      <div className="mb-6">
        <div className="flex justify-between items-center mb-4 border-b-2 border-zinc-100 dark:border-zinc-800 pb-1">
          <h2 className="text-xl font-black uppercase tracking-tight">
            Recent Quotes
          </h2>
          <button onClick={handleRefresh} className="text-zinc-400 active:rotate-180 transition-transform p-1">
            <RefreshCcw size={18} />
          </button>
        </div>

        {quotes.length > 0 ? (
          quotes.map(quote => (
            <RecentQuoteCard
              key={quote.id}
              quote={quote}
              onClick={onSelectQuote}
            />
          ))
        ) : (
          <div className="card text-center py-12 bg-zinc-50 dark:bg-zinc-900/50 border-dashed">
            <p className="text-zinc-400 font-bold">No quotes found. Start your first job!</p>
          </div>
        )}
      </div>

      {/* Floating Action Button */}
      <button
        onClick={onCreateQuote}
        className="fixed bottom-24 right-4 left-4 btn-primary h-16 shadow-xl flex gap-2 text-xl z-40"
      >
        <Plus size={28} strokeWidth={3} />
        CREATE NEW QUOTE
      </button>
    </div>
  );
};

export default Dashboard;
