import React from 'react';
import { View, Text, StyleSheet, FlatList, Image, TouchableOpacity, Linking, SafeAreaView } from 'react-native';
import { useDealsStore, Deal } from '../store/dealsStore';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';

export default function WishlistScreen() {
  const { wishlist, removeFromWishlist } = useDealsStore();
  const router = useRouter();

  const openAmazon = (asin: string) => {
    const affiliateUrl = `https://www.amazon.com/dp/${asin}?tag=ziilaa-20`;
    Linking.openURL(affiliateUrl);
  };

  const renderItem = ({ item }: { item: Deal }) => (
    <View style={styles.card}>
      <Image source={{ uri: item.imageUrl }} style={styles.thumbnail} />
      <View style={styles.details}>
        <Text style={styles.title} numberOfLines={2}>{item.title}</Text>
        <View style={styles.priceRow}>
          <Text style={styles.originalPrice}>{item.originalPrice}</Text>
          <Text style={styles.dealPrice}>{item.dealPrice}</Text>
        </View>
        <TouchableOpacity
          style={styles.viewButton}
          onPress={() => openAmazon(item.asin)}
        >
          <Text style={styles.viewText}>View on Amazon</Text>
        </TouchableOpacity>
      </View>
      <TouchableOpacity
        style={styles.deleteButton}
        onPress={() => removeFromWishlist(item.asin)}
      >
        <Ionicons name="trash-outline" size={24} color="#FF4757" />
      </TouchableOpacity>
    </View>
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => router.back()} style={styles.backButton}>
          <Ionicons name="arrow-back" size={28} color="#FFF" />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>My Deals 💚</Text>
        <Text style={styles.count}>{wishlist.length} items</Text>
      </View>

      {wishlist.length === 0 ? (
        <View style={styles.emptyContainer}>
          <Ionicons name="heart-dislike-outline" size={80} color="#2A2A4A" />
          <Text style={styles.emptyTitle}>No saved deals yet.</Text>
          <TouchableOpacity
            style={styles.emptyButton}
            onPress={() => router.push('/')}
          >
            <Text style={styles.emptyButtonText}>Start Swiping! 🔥</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={wishlist}
          renderItem={renderItem}
          keyExtractor={item => item.asin}
          contentContainerStyle={styles.listContent}
        />
      )}

      <View style={styles.disclosure}>
        <Text style={styles.disclosureText}>
          As an Amazon Associate, DealSwipe earns from qualifying purchases.
        </Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0D0D1A',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#1A1A2E',
  },
  backButton: {
    marginRight: 15,
  },
  headerTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#FFF',
    flex: 1,
  },
  count: {
    fontSize: 16,
    color: '#00F5A0',
    fontWeight: '600',
  },
  listContent: {
    padding: 20,
  },
  card: {
    flexDirection: 'row',
    backgroundColor: '#1A1A2E',
    borderRadius: 15,
    padding: 12,
    marginBottom: 15,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#2A2A4A',
  },
  thumbnail: {
    width: 80,
    height: 80,
    borderRadius: 10,
    backgroundColor: '#0D0D1A',
  },
  details: {
    flex: 1,
    marginLeft: 15,
  },
  title: {
    color: '#FFF',
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 5,
  },
  priceRow: {
    flexDirection: 'row',
    alignItems: 'baseline',
    marginBottom: 8,
  },
  originalPrice: {
    color: '#A0A0B0',
    fontSize: 12,
    textDecorationLine: 'line-through',
    marginRight: 8,
  },
  dealPrice: {
    color: '#00F5A0',
    fontSize: 16,
    fontWeight: 'bold',
  },
  viewButton: {
    alignSelf: 'flex-start',
    backgroundColor: 'rgba(255, 107, 53, 0.1)',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 15,
    borderWidth: 1,
    borderColor: '#FF6B35',
  },
  viewText: {
    color: '#FF6B35',
    fontSize: 12,
    fontWeight: '600',
  },
  deleteButton: {
    padding: 10,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 40,
  },
  emptyTitle: {
    color: '#A0A0B0',
    fontSize: 18,
    marginVertical: 20,
    textAlign: 'center',
  },
  emptyButton: {
    backgroundColor: '#00F5A0',
    paddingHorizontal: 25,
    paddingVertical: 12,
    borderRadius: 25,
  },
  emptyButtonText: {
    color: '#0D0D1A',
    fontWeight: 'bold',
    fontSize: 16,
  },
  disclosure: {
    padding: 15,
    backgroundColor: '#16213E',
    alignItems: 'center',
  },
  disclosureText: {
    color: '#A0A0B0',
    fontSize: 10,
    textAlign: 'center',
    fontStyle: 'italic',
  },
});
