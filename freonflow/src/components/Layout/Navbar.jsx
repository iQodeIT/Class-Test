import React from 'react';
import { Wind, Menu } from 'lucide-react';

const Navbar = ({ onLogoClick }) => {
  return (
    <nav className="sticky top-0 z-50 bg-white border-b-4 border-black px-4 py-3 flex items-center justify-between shadow-md">
      <div
        className="flex items-center gap-2 cursor-pointer active:opacity-70"
        onClick={onLogoClick}
      >
        <div className="bg-hvac-blue p-1 rounded-md">
          <Wind className="text-white" size={24} />
        </div>
        <span className="text-2xl font-black tracking-tighter">FreonFlow</span>
      </div>

      <button className="p-2 active:bg-gray-100 rounded-full">
        <Menu size={28} />
      </button>
    </nav>
  );
};

export default Navbar;
