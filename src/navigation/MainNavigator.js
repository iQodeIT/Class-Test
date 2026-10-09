import React, { useState } from 'react';
import { StyleSheet, View, Text, TouchableOpacity, SafeAreaView, StatusBar } from 'react-native';
import { Building2, BookOpen, Settings as SettingsIcon } from 'lucide-react-native';
import { useApp } from '../context/AppContext';

// Screens
import PropertiesHomeScreen from '../screens/PropertiesHomeScreen';
import ResourcesScreen from '../screens/ResourcesScreen';
import SettingsScreen from '../screens/SettingsScreen';
import AddPropertyModal from '../screens/AddPropertyModal';
import PropertyDetailScreen from '../screens/PropertyDetailScreen';
import RoomCaptureScreen from '../screens/RoomCaptureScreen';
import CompareViewScreen from '../screens/CompareViewScreen';
import DemandLetterScreen from '../screens/DemandLetterScreen';
import PaywallModal from '../components/PaywallModal';

export default function MainNavigator() {
  const { colors, isDarkMode } = useApp();
  const [activeTab, setActiveTab] = useState('Properties'); // 'Properties', 'Resources', 'Settings'

  // Navigation Stack State
  const [currentScreen, setCurrentScreen] = useState('TAB_NAV'); // 'TAB_NAV', 'ADD_PROPERTY', 'PROPERTY_DETAIL', 'ROOM_CAPTURE', 'COMPARE_VIEW', 'DEMAND_LETTER'
  const [screenParams, setScreenParams] = useState({});
  const [paywallVisible, setPaywallVisible] = useState(false);

  const navigateTo = (screenName, params = {}) => {
    setScreenParams(params);
    setCurrentScreen(screenName);
  };

  const goBack = () => {
    setCurrentScreen('TAB_NAV');
  };

  const openPaywall = () => {
    setPaywallVisible(true);
  };

  // Render current active screen
  const renderContent = () => {
    if (currentScreen === 'ADD_PROPERTY') {
      return <AddPropertyModal goBack={goBack} navigateTo={navigateTo} />;
    }
    if (currentScreen === 'PROPERTY_DETAIL') {
      return <PropertyDetailScreen params={screenParams} goBack={goBack} navigateTo={navigateTo} openPaywall={openPaywall} />;
    }
    if (currentScreen === 'ROOM_CAPTURE') {
      return <RoomCaptureScreen params={screenParams} goBack={goBack} navigateTo={navigateTo} openPaywall={openPaywall} />;
    }
    if (currentScreen === 'COMPARE_VIEW') {
      return <CompareViewScreen params={screenParams} goBack={goBack} navigateTo={navigateTo} openPaywall={openPaywall} />;
    }
    if (currentScreen === 'DEMAND_LETTER') {
      return <DemandLetterScreen params={screenParams} goBack={goBack} openPaywall={openPaywall} />;
    }

    // Default Tab Screens
    switch (activeTab) {
      case 'Properties':
        return <PropertiesHomeScreen navigateTo={navigateTo} openPaywall={openPaywall} />;
      case 'Resources':
        return <ResourcesScreen navigateTo={navigateTo} />;
      case 'Settings':
        return <SettingsScreen navigateTo={navigateTo} openPaywall={openPaywall} />;
      default:
        return <PropertiesHomeScreen navigateTo={navigateTo} openPaywall={openPaywall} />;
    }
  };

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />

      {/* Main Screen Body */}
      <View style={styles.body}>{renderContent()}</View>

      {/* Bottom Navigation Bar (Visible on Tab Views) */}
      {currentScreen === 'TAB_NAV' && (
        <View style={[styles.tabBar, { backgroundColor: colors.surface, borderTopColor: colors.border }]}>
          <TouchableOpacity
            style={styles.tabItem}
            onPress={() => setActiveTab('Properties')}
            activeOpacity={0.7}
          >
            <Building2
              size={24}
              color={activeTab === 'Properties' ? colors.primary : colors.textMuted}
              strokeWidth={activeTab === 'Properties' ? 2.5 : 1.8}
            />
            <Text
              style={[
                styles.tabLabel,
                { color: activeTab === 'Properties' ? colors.primary : colors.textMuted }
              ]}
            >
              Properties
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.tabItem}
            onPress={() => setActiveTab('Resources')}
            activeOpacity={0.7}
          >
            <BookOpen
              size={24}
              color={activeTab === 'Resources' ? colors.primary : colors.textMuted}
              strokeWidth={activeTab === 'Resources' ? 2.5 : 1.8}
            />
            <Text
              style={[
                styles.tabLabel,
                { color: activeTab === 'Resources' ? colors.primary : colors.textMuted }
              ]}
            >
              Resources
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.tabItem}
            onPress={() => setActiveTab('Settings')}
            activeOpacity={0.7}
          >
            <SettingsIcon
              size={24}
              color={activeTab === 'Settings' ? colors.primary : colors.textMuted}
              strokeWidth={activeTab === 'Settings' ? 2.5 : 1.8}
            />
            <Text
              style={[
                styles.tabLabel,
                { color: activeTab === 'Settings' ? colors.primary : colors.textMuted }
              ]}
            >
              Settings
            </Text>
          </TouchableOpacity>
        </View>
      )}

      {/* Global Paywall Modal */}
      <PaywallModal visible={paywallVisible} onClose={() => setPaywallVisible(false)} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  body: {
    flex: 1,
  },
  tabBar: {
    height: 64,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    borderTopWidth: 1,
    paddingBottom: 6,
    paddingTop: 6,
    elevation: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 0.05,
    shadowRadius: 4,
  },
  tabItem: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 20,
    paddingVertical: 4,
  },
  tabLabel: {
    fontSize: 12,
    fontWeight: '600',
    marginTop: 4,
  }
});
