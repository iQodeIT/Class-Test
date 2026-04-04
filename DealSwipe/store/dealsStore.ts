import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import AsyncStorage from '@react-native-async-storage/async-storage';
import dealsData from '../data/deals.json';

export interface Deal {
  id: string;
  asin: string;
  title: string;
  originalPrice: string;
  dealPrice: string;
  discount: string;
  rating: number;
  reviewCount: string;
  category: string;
  badge: string;
  isPrime: boolean;
  imageUrl: string;
  affiliateUrl: string;
  timeLeft: string | null;
  percentClaimed: number | null;
}

interface DealsState {
  deals: Deal[];
  wishlist: Deal[];
  currentIndex: number;
  selectedCategory: string;
  addToWishlist: (deal: Deal) => void;
  removeFromWishlist: (asin: string) => void;
  nextDeal: () => void;
  setSelectedCategory: (category: string) => void;
  undoLastSwipe: () => void;
  lastSwipedDeal: Deal | null;
}

export const useDealsStore = create<DealsState>()(
  persist(
    (set, get) => ({
      deals: dealsData,
      wishlist: [],
      currentIndex: 0,
      selectedCategory: 'All',
      lastSwipedDeal: null,

      addToWishlist: (deal) => {
        set((state) => {
          if (state.wishlist.some((item) => item.asin === deal.asin)) {
            return state;
          }
          return { wishlist: [deal, ...state.wishlist] };
        });
      },

      removeFromWishlist: (asin) => {
        set((state) => ({
          wishlist: state.wishlist.filter((item) => item.asin !== asin),
        }));
      },

      nextDeal: () => {
        set((state) => {
          const filteredDeals = state.selectedCategory === 'All'
            ? state.deals
            : state.deals.filter(d => d.category === state.selectedCategory);

          const currentDeal = filteredDeals[state.currentIndex];

          return {
            currentIndex: state.currentIndex + 1,
            lastSwipedDeal: currentDeal
          };
        });
      },

      setSelectedCategory: (category) => {
        set({ selectedCategory: category, currentIndex: 0 });
      },

      undoLastSwipe: () => {
        set((state) => {
          if (state.currentIndex > 0) {
            return { currentIndex: state.currentIndex - 1, lastSwipedDeal: null };
          }
          return state;
        });
      }
    }),
    {
      name: 'deals-storage',
      storage: createJSONStorage(() => AsyncStorage),
      partialize: (state) => ({ wishlist: state.wishlist }),
    }
  )
);
