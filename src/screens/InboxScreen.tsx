import React, { useState } from 'react';
import { View, Text, FlatList } from 'react-native';
import { ScreenContainer } from '../components/ScreenContainer';
import { Input } from '../components/Input';
import { Button } from '../components/Button';
import { Card } from '../components/Card';
import { Inbox as InboxIcon } from 'lucide-react-native';

export const InboxScreen = () => {
  const [items, setItems] = useState<string[]>([]);
  const [newItem, setNewItem] = useState('');

  const addItem = () => {
    if (newItem.trim()) {
      setItems([newItem, ...items]);
      setNewItem('');
    }
  };

  return (
    <ScreenContainer>
      <Text className="text-3xl font-bold text-taupe-900 mb-6 font-playfair">Inbox</Text>

      <View className="flex-row gap-2 mb-6">
        <Input
          value={newItem}
          onChangeText={setNewItem}
          placeholder="Capture a thought..."
          className="flex-1 mb-0"
        />
        <Button title="Add" onPress={addItem} className="h-[50px] self-end" />
      </View>

      {items.length === 0 ? (
        <View className="flex-1 justify-center items-center opacity-40">
          <InboxIcon size={64} color="#847364" />
          <Text className="text-taupe-500 mt-4 font-inter">Your inbox is empty</Text>
        </View>
      ) : (
        <FlatList
          data={items}
          keyExtractor={(item, index) => index.toString()}
          renderItem={({ item }) => (
            <Card className="mb-3">
              <Text className="text-taupe-800 font-inter">{item}</Text>
            </Card>
          )}
        />
      )}
    </ScreenContainer>
  );
};
