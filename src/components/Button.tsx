import React from 'react';
import { TouchableOpacity, Text, View } from 'react-native';

interface ButtonProps {
  title: string;
  onPress: () => void;
  variant?: 'primary' | 'secondary' | 'outline';
  className?: string;
}

export const Button: React.FC<ButtonProps> = ({ title, onPress, variant = 'primary', className = '' }) => {
  const getVariantClass = () => {
    switch (variant) {
      case 'primary': return 'bg-sage-600';
      case 'secondary': return 'bg-clay-500';
      case 'outline': return 'border border-sage-600';
      default: return 'bg-sage-600';
    }
  };

  const getTextClass = () => {
    switch (variant) {
      case 'outline': return 'text-sage-700';
      default: return 'text-white';
    }
  };

  return (
    <TouchableOpacity
      onPress={onPress}
      className={`px-6 py-3 rounded-xl items-center justify-center ${getVariantClass()} ${className}`}
    >
      <Text className={`font-semibold text-base ${getTextClass()}`}>{title}</Text>
    </TouchableOpacity>
  );
};
