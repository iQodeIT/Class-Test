import React from 'react';
import { View, Text } from 'react-native';

interface CardProps {
  children: React.ReactNode;
  className?: string;
}

export const Card: React.FC<CardProps> = ({ children, className = '' }) => {
  const hasBg = className.includes('bg-');
  return (
    <View className={`${hasBg ? '' : 'bg-white'} rounded-2xl p-4 shadow-sm border border-taupe-100 ${className}`}>
      {children}
    </View>
  );
};
