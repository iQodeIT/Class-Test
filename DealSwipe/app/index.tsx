import React, { useMemo, useCallback } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, SafeAreaView, Platform, StatusBar } from 'react-native';
import { useDealsStore, Deal } from '../store/dealsStore';
import { DealCard } from '../components/DealCard';
import { SwipeButtons } from '../components/SwipeButtons';
import { EmptyState } from '../components/EmptyState';
import { Ionicons } from '@expo/vector-icons';
import { Link } from 'expo-router';

const CATEGORIES = ['All', 'Electronics', 'Kitchen', 'Fitness', 'Home & Garden', 'Beauty', 'Books', 'Gaming', 'Clothing'];

export default function SwipeScreen() {
  const {
    deals,
    currentIndex,
    selectedCategory,
    setSelectedCategory,
    nextDeal,
    addToWishlist,
    undoLastSwipe,
    wishlist
  } = useDealsStore();

  const filteredDeals = useMemo(() => {
    return selectedCategory === 'All'
      ? deals
      : deals.filter(d => d.category === selectedCategory);
  }, [deals, selectedCategory]);

  const dealsRemaining = Math.max(0, filteredDeals.length - currentIndex);

  const handleSwipeLeft = useCallback(() => {
    nextDeal();
  }, [nextDeal]);

  const handleSwipeRight = useCallback(() => {
    const currentDeal = filteredDeals[currentIndex];
    if (currentDeal) {
      addToWishlist(currentDeal);
    }
    nextDeal();
  }, [filteredDeals, currentIndex, addToWishlist, nextDeal]);

  if (dealsRemaining === 0) {
    return <EmptyState />;
  }

  // We want to render up to 3 cards for the stack effect
  const visibleDeals = filteredDeals.slice(currentIndex, currentIndex + 3).reverse();

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" />

      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.logo}>DealSwipe 🔥</Text>
        <Link href="/wishlist" asChild>
          <TouchableOpacity style={styles.wishlistIcon}>
            <Ionicons name="heart" size={28} color="#00F5A0" />
            {wishlist.length > 0 && (
              <View style={styles.badge}>
                <Text style={styles.badgeText}>{wishlist.length}</Text>
              </View>
            )}
          </TouchableOpacity>
        </Link>
      </View>

      {/* Category Bar */}
      <View style={styles.categoryContainer}>
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.categoryContent}
        >
          {CATEGORIES.map(cat => {
            const count = cat === 'All' ? deals.length : deals.filter(d => d.category === cat).length;
            if (count === 0 && cat !== 'All') return null;

            return (
              <TouchableOpacity
                key={cat}
                style={[styles.categoryPill, selectedCategory === cat && styles.categoryPillActive]}
                onPress={() => setSelectedCategory(cat)}
              >
                <Text style={[styles.categoryText, selectedCategory === cat && styles.categoryTextActive]}>
                  {cat} ({count})
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>
      </View>

      {/* Card Stack */}
      <View style={styles.stackContainer}>
        {visibleDeals.map((deal, index) => {
          // The top card is the last one in the reversed visibleDeals array
          const isTop = index === visibleDeals.length - 1;
          const stackIndex = (visibleDeals.length - 1) - index;

          return (
            <DealCard
              key={deal.asin}
              deal={deal}
              isTop={isTop}
              stackIndex={stackIndex}
              onSwipeLeft={handleSwipeLeft}
              onSwipeRight={handleSwipeRight}
            />
          );
        })}
      </View>

      {/* Footer / Actions */}
      <View style={styles.footer}>
        <SwipeButtons
          onPressLeft={handleSwipeLeft}
          onPressRight={handleSwipeRight}
          onPressUndo={undoLastSwipe}
        />
        <Text style={styles.counter}>{dealsRemaining} deals left</Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0D0D1A',
    paddingTop: Platform.OS === 'android' ? StatusBar.currentHeight : 0,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 15,
  },
  logo: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#FFF',
  },
  wishlistIcon: {
    position: 'relative',
    padding: 5,
  },
  badge: {
    position: 'absolute',
    top: 0,
    right: 0,
    backgroundColor: '#FF4757',
    borderRadius: 10,
    minWidth: 18,
    height: 18,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 4,
  },
  badgeText: {
    color: '#FFF',
    fontSize: 10,
    fontWeight: 'bold',
  },
  categoryContainer: {
    height: 50,
    marginBottom: 10,
  },
  categoryContent: {
    paddingHorizontal: 15,
    alignItems: 'center',
  },
  categoryPill: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: '#1A1A2E',
    marginHorizontal: 5,
    borderWidth: 1,
    borderColor: '#2A2A4A',
  },
  categoryPillActive: {
    backgroundColor: '#00F5A0',
    borderColor: '#00F5A0',
  },
  categoryText: {
    color: '#A0A0B0',
    fontSize: 14,
    fontWeight: '600',
  },
  categoryTextActive: {
    color: '#0D0D1A',
  },
  stackContainer: {
    flex: 1,
    marginVertical: 10,
  },
  footer: {
    paddingBottom: 20,
    alignItems: 'center',
  },
  counter: {
    color: '#A0A0B0',
    fontSize: 14,
    fontWeight: '500',
  },
});
