import React from 'react';
import { Plus, ChevronRight, FileText, Clock, CheckCircle } from 'lucide-react';

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
          <p className="text-gray-600 font-medium">{quote.date}</p>
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
  return (
    <div className="p-4 pb-32">
      <div className="mb-8">
        <h1 className="text-3xl font-black mb-2">Welcome Back!</h1>
        <p className="text-gray-600 font-medium italic">"Get in, get paid, get home."</p>
      </div>

      <div className="mb-6">
        <h2 className="text-xl font-black uppercase tracking-tight mb-4 border-b-2 border-black pb-1">
          Recent Quotes
        </h2>
        {quotes.length > 0 ? (
          quotes.map(quote => (
            <RecentQuoteCard
              key={quote.id}
              quote={quote}
              onClick={onSelectQuote}
            />
          ))
        ) : (
          <div className="card text-center py-12 bg-gray-50 border-dashed">
            <p className="text-gray-500 font-bold">No quotes found. Start your first job!</p>
          </div>
        )}
      </div>

      {/* Floating Action Button */}
      <button
        onClick={onCreateQuote}
        className="fixed bottom-6 right-4 left-4 btn-primary h-16 shadow-[6px_6px_0px_0px_rgba(0,0,0,1)] flex gap-2 text-xl"
      >
        <Plus size={28} strokeWidth={3} />
        CREATE NEW QUOTE
      </button>
    </div>
  );
};

export default Dashboard;
