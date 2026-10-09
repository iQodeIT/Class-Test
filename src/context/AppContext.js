import React, { createContext, useContext, useState, useEffect } from 'react';
import { useColorScheme } from 'react-native';
import { COLORS } from '../theme/theme';
import { StorageService } from '../services/StorageService';

const AppContext = createContext();

export const AppProvider = ({ children }) => {
  const systemColorScheme = useColorScheme();
  const [themeMode, setThemeMode] = useState('system'); // 'light', 'dark', 'system'
  const [isOnboarded, setIsOnboarded] = useState(null);
  const [proStatus, setProStatus] = useState({ isPro: false, planType: 'free' });
  const [properties, setProperties] = useState([]);
  const [activeProperty, setActiveProperty] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const activeColorScheme = themeMode === 'system' ? (systemColorScheme || 'light') : themeMode;
  const colors = COLORS[activeColorScheme] || COLORS.light;

  const loadInitialData = async () => {
    setIsLoading(true);
    try {
      const onboarded = await StorageService.isOnboardingCompleted();
      setIsOnboarded(onboarded);

      const pro = await StorageService.getProStatus();
      setProStatus(pro);

      const props = await StorageService.getProperties();
      setProperties(props);
    } catch (e) {
      console.error('Failed to load initial state:', e);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadInitialData();
  }, []);

  const refreshProperties = async () => {
    const props = await StorageService.getProperties();
    setProperties(props);
    return props;
  };

  const completeOnboarding = async () => {
    await StorageService.setOnboardingCompleted(true);
    setIsOnboarded(true);
  };

  const updateProStatus = async (isPro, planType) => {
    const updated = await StorageService.setProStatus(isPro, planType);
    setProStatus(updated);
  };

  return (
    <AppContext.Provider
      value={{
        colors,
        isDarkMode: activeColorScheme === 'dark',
        themeMode,
        setThemeMode,
        isOnboarded,
        completeOnboarding,
        proStatus,
        updateProStatus,
        properties,
        refreshProperties,
        activeProperty,
        setActiveProperty,
        isLoading
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useApp = () => useContext(AppContext);
