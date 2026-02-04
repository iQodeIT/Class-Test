import React from 'react';
import { TextInput, View, Text } from 'react-native';

interface InputProps {
  label?: string;
  value: string;
  onChangeText: (text: string) => void;
  placeholder?: string;
  className?: string;
}

export const Input: React.FC<InputProps> = ({ label, value, onChangeText, placeholder, className = '' }) => {
  return (
    <View className={`mb-4 ${className}`}>
      {label && <Text className="text-taupe-700 font-semibold mb-1 font-inter">{label}</Text>}
      <TextInput
        value={value}
        onChangeText={onChangeText}
        placeholder={placeholder}
        className="bg-white border border-taupe-200 rounded-xl px-4 py-3 font-inter text-taupe-900"
        placeholderTextColor="#9d8e7f"
      />
    </View>
  );
};
