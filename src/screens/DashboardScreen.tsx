import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity } from 'react-native';
import { ScreenContainer } from '../components/ScreenContainer';
import { Card } from '../components/Card';
import { Button } from '../components/Button';
import { Zap, Battery, CheckCircle2, Clock } from 'lucide-react-native';

export const DashboardScreen = () => {
  const [energy, setEnergy] = useState<'high' | 'low'>('high');

  return (
    <ScreenContainer>
      <ScrollView showsVerticalScrollIndicator={false}>
        <View className="flex-row justify-between items-center mb-6">
          <View>
            <Text className="text-taupe-500 text-sm font-inter">Monday, Oct 14</Text>
            <Text className="text-3xl font-bold text-taupe-900 font-playfair">Good Morning</Text>
          </View>
          <TouchableOpacity
            onPress={() => setEnergy(energy === 'high' ? 'low' : 'high')}
            className={`p-3 rounded-full ${energy === 'high' ? 'bg-clay-100' : 'bg-sage-100'}`}
          >
            {energy === 'high' ? <Zap size={24} color="#d2691e" /> : <Battery size={24} color="#688667" />}
          </TouchableOpacity>
        </View>

        <View className="flex-row gap-4 mb-6">
          <Card className="flex-1 bg-sage-600 border-0">
            <Text className="text-sage-50 text-xs font-inter mb-1">Daily Goal</Text>
            <Text className="text-white text-lg font-semibold font-inter">85% Done</Text>
          </Card>
          <Card className="flex-1 bg-taupe-700 border-0">
            <Text className="text-taupe-50 text-xs font-inter mb-1">Velocity</Text>
            <Text className="text-white text-lg font-semibold font-inter">12 pts/day</Text>
          </Card>
        </View>

        <Text className="text-xl font-bold text-taupe-800 mb-4 font-playfair">Today's Schedule</Text>

        <Card className="mb-3">
          <View className="flex-row items-center gap-3">
            <View className="w-12 items-center">
              <Text className="text-taupe-900 font-bold font-inter">09:00</Text>
            </View>
            <View className="flex-1 border-l border-taupe-100 pl-3">
              <Text className="text-taupe-800 font-semibold font-inter">Deep Work: UI Design</Text>
              <Text className="text-taupe-500 text-xs font-inter">2 hours • High Energy</Text>
            </View>
            <CheckCircle2 size={20} color="#abc1aa" />
          </View>
        </Card>

        <Card className="mb-6">
          <View className="flex-row items-center gap-3">
            <View className="w-12 items-center">
              <Text className="text-taupe-400 font-inter">11:30</Text>
            </View>
            <View className="flex-1 border-l border-taupe-100 pl-3">
              <Text className="text-taupe-800 font-semibold font-inter">Team Sync</Text>
              <Text className="text-taupe-500 text-xs font-inter">30 mins • Low Energy</Text>
            </View>
            <View className="w-5 h-5 rounded-full border border-taupe-200" />
          </View>
        </Card>

        <View className="flex-row justify-between items-center mb-4">
          <Text className="text-xl font-bold text-taupe-800 font-playfair">Daily Rituals</Text>
          <Button title="View All" onPress={() => {}} variant="outline" className="py-1 px-3" />
        </View>

        <View className="flex-row gap-3 mb-6">
          {['Meditation', 'Hydration', 'Reading'].map((habit) => (
            <TouchableOpacity key={habit} className="bg-white border border-taupe-100 rounded-xl px-4 py-3 flex-1 items-center">
              <Text className="text-taupe-700 font-medium text-xs font-inter">{habit}</Text>
            </TouchableOpacity>
          ))}
        </View>

        <Card className="bg-clay-50 border-clay-100 mb-10">
          <View className="flex-row justify-between items-center">
            <View>
              <Text className="text-clay-800 font-bold font-inter">Focus Session</Text>
              <Text className="text-clay-600 text-xs font-inter">Pomodoro Timer • 25:00</Text>
            </View>
            <TouchableOpacity className="bg-clay-500 p-2 rounded-lg">
              <Clock size={20} color="white" />
            </TouchableOpacity>
          </View>
        </Card>
      </ScrollView>
    </ScreenContainer>
  );
};
