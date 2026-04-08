import React, { useState, useEffect } from 'react';
import { Trash2, Plus, Sparkles, Save, ArrowLeft, ChevronRight } from 'lucide-react';
import { parseJobDescription } from '../../utils/ai-parser';

const RapidQuoteForm = ({ onSave, onBack, initialClients = [] }) => {
  const [clientId, setClientId] = useState('');
  const [showNewClient, setShowNewClient] = useState(false);
  const [newClient, setNewClient] = useState({ full_name: '', address: '' });
  const [magicInput, setMagicInput] = useState('');
  const [items, setItems] = useState([{ id: 1, description: '', quantity: 1, unit_price: 0 }]);
  const [isParsing, setIsParsing] = useState(false);

  const total = items.reduce((sum, item) => sum + (item.quantity * item.unit_price), 0);

  const addItem = () => {
    setItems([...items, { id: Date.now(), description: '', quantity: 1, unit_price: 0 }]);
  };

  const removeItem = (id) => {
    if (items.length > 1) {
      setItems(items.filter(item => item.id !== id));
    }
  };

  const updateItem = (id, field, value) => {
    setItems(items.map(item =>
      item.id === id ? { ...item, [field]: field === 'description' ? value : parseFloat(value) || 0 } : item
    ));
  };

  const handleMagicParse = async () => {
    if (!magicInput.trim()) return;
    setIsParsing(true);
    try {
      const parsedItems = await parseJobDescription(magicInput);
      const itemsWithIds = parsedItems.map(item => ({ ...item, id: Math.random() }));
      // Prepend parsed items or replace? Let's prepend.
      setItems([...itemsWithIds, ...items.filter(i => i.description !== '')]);
      setMagicInput('');
    } catch (error) {
      console.error("AI Parse failed", error);
    } finally {
      setIsParsing(false);
    }
  };

  return (
    <div className="p-4 pb-40">
      <button onClick={onBack} className="flex items-center gap-1 font-bold mb-6 active:opacity-50">
        <ArrowLeft size={20} /> BACK TO DASHBOARD
      </button>

      <h1 className="text-3xl font-black mb-6">New Quote</h1>

      {/* Section A: Client */}
      <section className="mb-8">
        <h2 className="text-sm font-black uppercase tracking-widest text-gray-500 mb-2">Section A: Client</h2>
        {!showNewClient ? (
          <div className="flex flex-col gap-2">
            <div className="relative">
              <select
                className="input-field appearance-none"
                value={clientId}
                onChange={(e) => setClientId(e.target.value)}
              >
                <option value="">Select Existing Client</option>
                {initialClients.map(c => (
                  <option key={c.id} value={c.id}>{c.full_name}</option>
                ))}
              </select>
              <div className="absolute right-4 top-1/2 -translate-y-1/2 pointer-events-none text-zinc-400">
                <ChevronRight size={20} className="rotate-90" />
              </div>
            </div>
            <button
              onClick={() => setShowNewClient(true)}
              className="text-hvac-blue font-bold text-left px-2"
            >
              + New Client
            </button>
          </div>
        ) : (
          <div className="card border-hvac-blue">
            <input
              className="input-field mb-2"
              placeholder="Client Name"
              value={newClient.full_name}
              onChange={(e) => setNewClient({...newClient, full_name: e.target.value})}
            />
            <input
              className="input-field mb-2"
              placeholder="Service Address"
              value={newClient.address}
              onChange={(e) => setNewClient({...newClient, address: e.target.value})}
            />
            <button
              onClick={() => setShowNewClient(false)}
              className="text-gray-500 font-bold"
            >
              Cancel
            </button>
          </div>
        )}
      </section>

      {/* Section B: Magic Input */}
      <section className="mb-8">
        <h2 className="text-sm font-black uppercase tracking-widest text-gray-500 mb-2">Section B: Magic Input</h2>
        <div className="relative">
          <textarea
            className="input-field min-h-[120px] pt-4"
            placeholder="e.g. Service call, added 2lbs of Freon and replaced a capacitor..."
            value={magicInput}
            onChange={(e) => setMagicInput(e.target.value)}
          />
          <button
            onClick={handleMagicParse}
            disabled={isParsing || !magicInput}
            className="absolute bottom-3 right-3 bg-black text-white px-4 py-2 rounded-lg font-bold flex items-center gap-2 active:scale-95 disabled:opacity-50"
          >
            {isParsing ? '...' : <><Sparkles size={18} /> MAGIC PARSE</>}
          </button>
        </div>
      </section>

      {/* Section C: Line Items */}
      <section className="mb-8">
        <h2 className="text-sm font-black uppercase tracking-widest text-gray-500 mb-2">Section C: Line Items</h2>
        {items.map((item, index) => (
          <div key={item.id} className="card mb-4 border-gray-300">
            <div className="flex justify-between mb-2">
              <span className="font-bold text-sm">ITEM #{index + 1}</span>
              <button onClick={() => removeItem(item.id)} className="text-red-500">
                <Trash2 size={20} />
              </button>
            </div>
            <input
              className="input-field mb-2"
              placeholder="Description"
              value={item.description}
              onChange={(e) => updateItem(item.id, 'description', e.target.value)}
            />
            <div className="grid grid-cols-2 gap-2">
              <div className="flex flex-col">
                <label className="text-[10px] font-bold uppercase">Qty</label>
                <input
                  type="number"
                  className="input-field"
                  value={item.quantity}
                  onChange={(e) => updateItem(item.id, 'quantity', e.target.value)}
                />
              </div>
              <div className="flex flex-col">
                <label className="text-[10px] font-bold uppercase">Price</label>
                <input
                  type="number"
                  className="input-field"
                  value={item.unit_price}
                  onChange={(e) => updateItem(item.id, 'unit_price', e.target.value)}
                />
              </div>
            </div>
          </div>
        ))}
        <button
          onClick={addItem}
          className="btn-secondary w-full border-2 border-dashed border-black bg-transparent"
        >
          <Plus size={20} className="mr-2" /> ADD LINE ITEM
        </button>
      </section>

      {/* Section D: Sticky Footer */}
      <div className="fixed bottom-0 left-0 right-0 bg-white border-t-4 border-black p-4 flex items-center justify-between shadow-[0_-10px_20px_rgba(0,0,0,0.1)]">
        <div>
          <p className="text-xs font-black uppercase text-gray-500">Total Estimate</p>
          <p className="text-3xl font-black">${total.toFixed(2)}</p>
        </div>
        <button
          onClick={() => onSave({ clientId, newClient, items, total })}
          className="btn-primary h-14 px-8 shadow-[4px_4px_0px_0px_rgba(0,0,0,1)]"
        >
          <Save size={24} className="mr-2" /> SAVE
        </button>
      </div>
    </div>
  );
};

export default RapidQuoteForm;
