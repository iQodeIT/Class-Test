import React from 'react';
import { View, TouchableOpacity, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';

interface SwipeButtonsProps {
  onPressLeft: () => void;
  onPressRight: () => void;
  onPressUndo: () => void;
}

export const SwipeButtons: React.FC<SwipeButtonsProps> = ({ onPressLeft, onPressRight, onPressUndo }) => {
  return (
    <View style={styles.container}>
      <TouchableOpacity style={[styles.button, styles.undoButton]} onPress={onPressUndo}>
        <Ionicons name="refresh" size={24} color="#FFD700" />
      </TouchableOpacity>

      <TouchableOpacity style={[styles.button, styles.leftButton]} onPress={onPressLeft}>
        <Ionicons name="close" size={32} color="#FF4757" />
      </TouchableOpacity>

      <TouchableOpacity style={[styles.button, styles.rightButton]} onPress={onPressRight}>
        <Ionicons name="heart" size={32} color="#00F5A0" />
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 20,
    paddingVertical: 20,
  },
  button: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: '#1A1A2E',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#2A2A4A',
    elevation: 5,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
  },
  undoButton: {
    width: 45,
    height: 45,
    borderRadius: 22.5,
  },
  leftButton: {
    width: 65,
    height: 65,
    borderRadius: 32.5,
  },
  rightButton: {
    width: 65,
    height: 65,
    borderRadius: 32.5,
  },
});
