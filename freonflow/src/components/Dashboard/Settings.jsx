import React from 'react';
import { User, Shield, Bell, Trash2, ArrowLeft } from 'lucide-react';

const Settings = ({ onBack }) => {
  const handleDeleteAccount = () => {
    if (window.confirm("Are you SURE you want to delete your account? This will erase all your quotes and data permanently.")) {
      alert("Account deletion request submitted.");
    }
  };

  return (
    <div className="p-4">
      <div className="flex items-center gap-2 mb-8">
        <h1 className="text-3xl font-black">Settings</h1>
      </div>

      <div className="space-y-4">
        <div className="card flex items-center gap-4">
          <div className="bg-hvac-blue/10 p-3 rounded-full text-hvac-blue">
            <User size={24} />
          </div>
          <div>
            <p className="font-bold">Profile Information</p>
            <p className="text-sm text-zinc-500">Edit your company name and phone</p>
          </div>
        </div>

        <div className="card flex items-center gap-4">
          <div className="bg-zinc-100 dark:bg-zinc-800 p-3 rounded-full">
            <Shield size={24} />
          </div>
          <div>
            <p className="font-bold">Security</p>
            <p className="text-sm text-zinc-500">Change password and MFA</p>
          </div>
        </div>

        <div className="card flex items-center gap-4">
          <div className="bg-zinc-100 dark:bg-zinc-800 p-3 rounded-full">
            <Bell size={24} />
          </div>
          <div>
            <p className="font-bold">Notifications</p>
            <p className="text-sm text-zinc-500">Quote approval alerts</p>
          </div>
        </div>

        <div className="pt-8">
          <h2 className="text-sm font-black uppercase tracking-widest text-zinc-500 mb-4 px-2">Danger Zone</h2>
          <button
            onClick={handleDeleteAccount}
            className="btn-danger w-full gap-2"
          >
            <Trash2 size={20} />
            DELETE ACCOUNT
          </button>
          <p className="text-[10px] text-zinc-500 mt-2 text-center px-4 uppercase font-bold tracking-tighter">
            Account deletion is permanent and cannot be undone.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Settings;
