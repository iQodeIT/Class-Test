import React from 'react';
import { View, Text, FlatList } from 'react-native';
import { ScreenContainer } from '../components/ScreenContainer';
import { Card } from '../components/Card';
import { ProgressBar } from '../components/ProgressBar';

const MOCK_PROJECTS = [
  { id: '1', name: 'Soulstice App', progress: 0.2, status: 'Active' },
  { id: '2', name: 'Home Renovation', progress: 0.6, status: 'Active' },
  { id: '3', name: 'Book Writing', progress: 0.0, status: 'Active' },
];

export const ProjectsScreen = () => {
  return (
    <ScreenContainer>
      <Text className="text-3xl font-bold text-taupe-900 mb-6 font-playfair">Projects</Text>

      <FlatList
        data={MOCK_PROJECTS}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <Card className="mb-4">
            <View className="flex-row justify-between items-center mb-3">
              <Text className="text-xl font-semibold text-taupe-800 font-inter">{item.name}</Text>
              <View className="bg-sage-100 px-2 py-1 rounded">
                <Text className="text-sage-700 text-xs font-inter">{item.status}</Text>
              </View>
            </View>
            <ProgressBar progress={item.progress} className="mb-2" />
            <Text className="text-taupe-500 text-sm font-inter">
              {Math.round(item.progress * 100)}% Complete
            </Text>
          </Card>
        )}
      />
    </ScreenContainer>
  );
};
