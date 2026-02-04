import React from 'react';
import { View } from 'react-native';

interface ProgressBarProps {
  progress: number; // 0 to 1
  className?: string;
}

export const ProgressBar: React.FC<ProgressBarProps> = ({ progress, className = '' }) => {
  return (
    <View className={`h-2 bg-taupe-100 rounded-full overflow-hidden ${className}`}>
      <View
        className="h-full bg-sage-500"
        style={{ width: `${Math.min(100, Math.max(0, progress * 100))}%` }}
      />
    </View>
  );
};
