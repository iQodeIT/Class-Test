import React from 'react';
import { SafeAreaView, StatusBar, View } from 'react-native';

interface ScreenContainerProps {
  children: React.ReactNode;
  className?: string;
  onLayout?: () => void;
}

export const ScreenContainer: React.FC<ScreenContainerProps> = ({ children, className = '', onLayout }) => {
  return (
    <SafeAreaView className={`flex-1 bg-sage-50 ${className}`} onLayout={onLayout}>
      <StatusBar barStyle="dark-content" />
      <View className="flex-1 px-4 py-2">
        {children}
      </View>
    </SafeAreaView>
  );
};
