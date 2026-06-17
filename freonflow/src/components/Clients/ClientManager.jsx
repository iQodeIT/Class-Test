import React, { useState } from 'react';
import { ArrowLeft, UserPlus, Phone, MapPin, Search, Plus } from 'lucide-react';

const ClientManager = ({ clients, onAddClient, onBack }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isAdding, setIsAdding] = useState(false);
  const [newClient, setNewClient] = useState({ full_name: '', address: '', phone: '' });

  const filteredClients = clients.filter(c =>
    c.full_name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    c.address?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!newClient.full_name) return;

    const success = await onAddClient(newClient);
    if (success) {
      setNewClient({ full_name: '', address: '', phone: '' });
      setIsAdding(false);
    }
  };

  return (
    <div className="p-4 pb-32">
      <button onClick={onBack} className="flex items-center gap-1 font-bold mb-6 active:opacity-50">
        <ArrowLeft size={20} /> BACK
      </button>

      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-black">Clients</h1>
        <button
          onClick={() => setIsAdding(!isAdding)}
          className="bg-hvac-blue text-white p-3 rounded-full shadow-lg active:scale-90 transition-transform"
        >
          {isAdding ? <ArrowLeft size={24} className="rotate-90" /> : <Plus size={24} />}
        </button>
      </div>

      {isAdding && (
        <form onSubmit={handleSubmit} className="card border-hvac-blue mb-8 animate-in slide-in-from-top-4 duration-300">
          <h2 className="text-sm font-black uppercase mb-4 text-hvac-blue flex items-center gap-2">
            <UserPlus size={16} /> Add New Client
          </h2>
          <div className="space-y-3">
            <input
              className="input-field"
              placeholder="Full Name *"
              required
              value={newClient.full_name}
              onChange={(e) => setNewClient({...newClient, full_name: e.target.value})}
            />
            <input
              className="input-field"
              placeholder="Phone Number"
              value={newClient.phone}
              onChange={(e) => setNewClient({...newClient, phone: e.target.value})}
            />
            <textarea
              className="input-field min-h-[80px]"
              placeholder="Service Address"
              value={newClient.address}
              onChange={(e) => setNewClient({...newClient, address: e.target.value})}
            />
            <button type="submit" className="btn-primary w-full h-14">
              SAVE CLIENT
            </button>
          </div>
        </form>
      )}

      <div className="relative mb-6">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-400" size={20} />
        <input
          className="input-field pl-12"
          placeholder="Search clients..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      <div className="space-y-4">
        {filteredClients.length > 0 ? (
          filteredClients.map(client => (
            <div key={client.id} className="card border-zinc-200">
              <h3 className="font-bold text-xl mb-2">{client.full_name}</h3>
              <div className="space-y-1">
                {client.phone && (
                  <div className="flex items-center gap-2 text-zinc-500 font-medium">
                    <Phone size={14} />
                    <span>{client.phone}</span>
                  </div>
                )}
                {client.address && (
                  <div className="flex items-center gap-2 text-zinc-500 font-medium">
                    <MapPin size={14} />
                    <span className="text-sm">{client.address}</span>
                  </div>
                )}
              </div>
            </div>
          ))
        ) : (
          <div className="text-center py-12 text-zinc-400 font-bold">
            No clients found.
          </div>
        )}
      </div>
    </div>
  );
};

export default ClientManager;
