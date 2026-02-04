import React, { useState } from 'react';
import { View, Text, ScrollView } from 'react-native';
import { ScreenContainer } from '../components/ScreenContainer';
import { Card } from '../components/Card';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Sparkles, ArrowRight } from 'lucide-react-native';

export const ReviewScreen = () => {
  const [step, setStep] = useState(1);
  const [reflection, setReflection] = useState('');

  const renderStep = () => {
    switch (step) {
      case 1:
        return (
          <View>
            <Text className="text-2xl font-bold text-taupe-900 mb-4 font-playfair">Daily Shutdown</Text>
            <Text className="text-taupe-600 mb-6 font-inter">Let's reflect on your day and clear your mind for tomorrow.</Text>

            <Card className="mb-6">
              <Text className="text-taupe-800 font-semibold mb-2 font-inter">What went well today?</Text>
              <Input
                value={reflection}
                onChangeText={setReflection}
                placeholder="Write your highlights..."
                className="mb-0"
              />
            </Card>

            <Button
              title="Next Step"
              onPress={() => setStep(2)}
              className="w-full"
            />
          </View>
        );
      case 2:
        return (
          <View>
            <Text className="text-2xl font-bold text-taupe-900 mb-4 font-playfair">Task Rollover</Text>
            <Text className="text-taupe-600 mb-6 font-inter">3 tasks were not completed. Move them to tomorrow?</Text>

            <Card className="mb-3 border-clay-100 bg-clay-50">
              <Text className="text-clay-800 font-inter">Refactor Soulstice DAL</Text>
            </Card>
            <Card className="mb-3 border-clay-100 bg-clay-50">
              <Text className="text-clay-800 font-inter">Write documentation</Text>
            </Card>
            <Card className="mb-6 border-clay-100 bg-clay-50">
              <Text className="text-clay-800 font-inter">Call client for feedback</Text>
            </Card>

            <View className="flex-row gap-4">
              <Button title="Back" onPress={() => setStep(1)} variant="outline" className="flex-1" />
              <Button title="Rollover All" onPress={() => setStep(3)} className="flex-1" />
            </View>
          </View>
        );
      case 3:
        return (
          <View className="items-center justify-center pt-10">
            <View className="bg-sage-100 p-6 rounded-full mb-6">
              <Sparkles size={64} color="#688667" />
            </View>
            <Text className="text-2xl font-bold text-taupe-900 mb-2 font-playfair">All Set!</Text>
            <Text className="text-taupe-500 text-center mb-8 font-inter">You've cleared your mind. Enjoy your evening and see you tomorrow.</Text>
            <Button title="Finish Review" onPress={() => setStep(1)} className="w-full" />
          </View>
        );
      default:
        return null;
    }
  };

  return (
    <ScreenContainer>
      <ScrollView>
        {renderStep()}
      </ScrollView>
    </ScreenContainer>
  );
};
