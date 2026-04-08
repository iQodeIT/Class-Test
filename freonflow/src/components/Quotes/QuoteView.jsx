import React from 'react';
import { Share2, ArrowLeft, Download, Send } from 'lucide-react';
import { generatePDF } from '../../utils/pdf-generator';

const QuoteView = ({ quote, onBack }) => {
  const handleShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: `Quote for ${quote.client_name}`,
          text: `Here is your HVAC quote from FreonFlow for $${quote.total_amount.toFixed(2)}.`,
          url: window.location.href,
        });
      } catch (error) {
        console.log('Error sharing:', error);
      }
    } else {
      alert("Sharing not supported on this browser. Copy the URL manually.");
    }
  };

  const handleDownload = () => {
    generatePDF('invoice-capture', `Quote_${quote.client_name.replace(/\s+/g, '_')}`);
  };

  return (
    <div className="bg-gray-100 min-h-screen pb-24" id="invoice-capture">
      <nav className="bg-white p-4 border-b-2 border-black flex justify-between items-center sticky top-0">
        <button onClick={onBack} className="p-2 active:bg-gray-100 rounded-full">
          <ArrowLeft size={24} />
        </button>
        <h1 className="font-black uppercase tracking-tighter">Quote Review</h1>
        <button onClick={handleShare} className="p-2 active:bg-gray-100 rounded-full text-hvac-blue">
          <Share2 size={24} />
        </button>
      </nav>

      <div className="p-4">
        {/* The "Paper" Invoice */}
        <div className="bg-white border-2 border-black p-6 shadow-lg min-h-[600px] flex flex-col">
          <div className="flex justify-between items-start mb-8">
            <div>
              <h2 className="text-2xl font-black text-hvac-blue">FreonFlow</h2>
              <p className="text-xs font-bold uppercase">Estimates & Service</p>
            </div>
            <div className="text-right">
              <h3 className="text-xl font-black uppercase">Estimate</h3>
              <p className="text-sm font-medium">#{quote.id.toString().slice(-5)}</p>
              <p className="text-sm font-medium">{quote.date}</p>
            </div>
          </div>

          <div className="mb-8 text-right">
            <h4 className="text-[10px] font-black uppercase text-gray-400 mb-1">Bill To:</h4>
            <p className="font-bold text-lg leading-tight">{quote.client_name}</p>
            <p className="text-sm text-gray-600 italic">{quote.address || 'Service Address on File'}</p>
          </div>

          <div className="flex-grow">
            <table className="w-full mb-8">
              <thead>
                <tr className="border-b-2 border-black">
                  <th className="text-left py-2 text-[10px] font-black uppercase">Description</th>
                  <th className="text-right py-2 text-[10px] font-black uppercase">Qty</th>
                  <th className="text-right py-2 text-[10px] font-black uppercase">Total</th>
                </tr>
              </thead>
              <tbody>
                {quote.items.map((item, idx) => (
                  <tr key={idx} className="border-b border-gray-100">
                    <td className="py-3 text-sm font-medium">{item.description}</td>
                    <td className="py-3 text-right text-sm">{item.quantity}</td>
                    <td className="py-3 text-right text-sm font-bold">${(item.quantity * item.unit_price).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="mt-auto pt-4 border-t-2 border-black flex justify-between items-end">
            <div>
              <p className="text-[10px] font-black uppercase">Notes</p>
              <p className="text-xs italic text-gray-500 max-w-[200px]">Estimate valid for 30 days. Work will begin upon approval.</p>
            </div>
            <div className="text-right">
              <p className="text-xs font-black uppercase">Total Amount</p>
              <p className="text-4xl font-black">${quote.total_amount.toFixed(2)}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="fixed bottom-0 left-0 right-0 p-4 bg-white border-t-2 border-black grid grid-cols-2 gap-4">
        <button onClick={handleDownload} className="btn-secondary">
          <Download size={20} className="mr-2" /> PDF
        </button>
        <button onClick={handleShare} className="btn-primary">
          <Send size={20} className="mr-2" /> SEND TO CLIENT
        </button>
      </div>
    </div>
  );
};

export default QuoteView;
