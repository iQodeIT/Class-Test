import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useDealsStore } from '../store/dealsStore';

export const EmptyState = () => {
  const setSelectedCategory = useDealsStore(state => state.setSelectedCategory);

  return (
    <View style={styles.container}>
      <View style={styles.iconContainer}>
        <Ionicons name="sparkles" size={80} color="#00F5A0" />
      </View>
      <Text style={styles.title}>No more deals! 🔥</Text>
      <Text style={styles.subtitle}>
        You've seen everything in this category. Check back later or try another filter!
      </Text>

      <TouchableOpacity
        style={styles.button}
        onPress={() => setSelectedCategory('All')}
      >
        <Text style={styles.buttonText}>Show All Deals</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 40,
    backgroundColor: '#0D0D1A',
  },
  iconContainer: {
    marginBottom: 20,
    padding: 20,
    borderRadius: 50,
    backgroundColor: 'rgba(0, 245, 160, 0.1)',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#FFF',
    marginBottom: 10,
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 16,
    color: '#A0A0B0',
    textAlign: 'center',
    lineHeight: 24,
    marginBottom: 30,
  },
  button: {
    backgroundColor: '#1A1A2E',
    paddingHorizontal: 30,
    paddingVertical: 15,
    borderRadius: 30,
    borderWidth: 1,
    borderColor: '#00F5A0',
  },
  buttonText: {
    color: '#00F5A0',
    fontSize: 16,
    fontWeight: 'bold',
  },
});
