import React, { useState } from 'react';
import { View, Text, Image, TouchableOpacity, Linking, StyleSheet, Dimensions, ActivityIndicator } from 'react-native';
import Animated, { useAnimatedStyle, interpolate, Extrapolation } from 'react-native-reanimated';
import { GestureDetector, Gesture } from 'react-native-gesture-handler';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { Deal } from '../store/dealsStore';
import { useSwipe } from '../hooks/useSwipe';

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');

interface DealCardProps {
  deal: Deal;
  isTop: boolean;
  stackIndex: number;
  onSwipeLeft: () => void;
  onSwipeRight: () => void;
}

export const DealCard: React.FC<DealCardProps> = ({ deal, isTop, stackIndex, onSwipeLeft, onSwipeRight }) => {
  const [imageLoaded, setImageLoaded] = useState(false);

  const { gesture, cardStyle, likeOpacity, nopeOpacity, translateX } = useSwipe({
    onSwipeLeft,
    onSwipeRight,
  });

  const openAmazon = () => {
    const affiliateUrl = `https://www.amazon.com/dp/${deal.asin}?tag=ziilaa-20`;
    Linking.openURL(affiliateUrl);
  };

  const stackStyle = useAnimatedStyle(() => {
    if (isTop) return {};

    // Background cards animate up and scale as the top card is swiped
    const scale = interpolate(
      Math.abs(translateX.value),
      [0, SCREEN_WIDTH / 2],
      [1 - stackIndex * 0.05, 1 - (stackIndex - 1) * 0.05],
      Extrapolation.CLAMP
    );

    const translateY = interpolate(
      Math.abs(translateX.value),
      [0, SCREEN_WIDTH / 2],
      [stackIndex * 15, (stackIndex - 1) * 15],
      Extrapolation.CLAMP
    );

    return {
      transform: [{ scale }, { translateY }],
      opacity: 1 - (stackIndex * 0.2),
    };
  });

  const renderStars = (rating: number) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <Ionicons
          key={i}
          name={i <= rating ? "star" : i - rating < 1 ? "star-half" : "star-outline"}
          size={14}
          color="#FFD700"
        />
      );
    }
    return stars;
  };

  return (
    <View style={styles.cardContainer} pointerEvents={isTop ? 'auto' : 'none'}>
      <GestureDetector gesture={isTop ? gesture : Gesture.Race()}>
        <Animated.View style={[styles.card, isTop ? cardStyle : stackStyle]}>
          <View style={styles.imageContainer}>
            {!imageLoaded && (
              <View style={styles.skeleton}>
                <ActivityIndicator color="#00F5A0" />
              </View>
            )}
            <Image
              source={{ uri: deal.imageUrl }}
              style={styles.image}
              resizeMode="cover"
              onLoad={() => setImageLoaded(true)}
            />
            <LinearGradient
              colors={['transparent', 'rgba(26, 26, 46, 0.8)', '#1A1A2E']}
              style={styles.gradient}
            />

            {/* Badge chip */}
            <View style={[styles.badge, { backgroundColor: deal.badge.includes('Lightning') ? '#FF4757' : '#9B59B6' }]}>
              <Text style={styles.badgeText}>
                {deal.badge.includes('Lightning') ? '⚡ ' : '🏷️ '}
                {deal.badge}
              </Text>
            </View>

            {/* Prime badge */}
            {deal.isPrime && (
              <View style={styles.primeBadge}>
                <Text style={styles.primeText}>prime</Text>
              </View>
            )}

            {/* Discount circle */}
            <View style={styles.discountCircle}>
              <Text style={styles.discountText}>{deal.discount}</Text>
              <Text style={styles.offText}>OFF</Text>
            </View>
          </View>

          <View style={styles.content}>
            <Text style={styles.title} numberOfLines={2}>{deal.title}</Text>

            <View style={styles.ratingRow}>
              <View style={styles.stars}>{renderStars(deal.rating)}</View>
              <Text style={styles.reviewCount}>({deal.reviewCount})</Text>
            </View>

            <View style={styles.priceRow}>
              <Text style={styles.originalPrice}>{deal.originalPrice}</Text>
              <Text style={styles.dealPrice}>{deal.dealPrice}</Text>
            </View>

            {deal.timeLeft && (
              <Text style={styles.timer}>⏰ Ends in {deal.timeLeft}</Text>
            )}

            {deal.percentClaimed !== null && (
              <View style={styles.progressContainer}>
                <View style={[styles.progressBar, { width: `${deal.percentClaimed}%` }]} />
                <Text style={styles.progressText}>{deal.percentClaimed}% claimed</Text>
              </View>
            )}

            <TouchableOpacity style={styles.cta} onPress={openAmazon}>
              <LinearGradient
                colors={['#FF6B35', '#FF8C00']}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={styles.ctaGradient}
              >
                <Text style={styles.ctaText}>View on Amazon →</Text>
              </LinearGradient>
            </TouchableOpacity>
          </View>

          {/* Overlays */}
          {isTop && (
            <>
              <Animated.View style={[styles.overlay, styles.likeOverlay, likeOpacity]}>
                <Text style={styles.likeText}>SAVED 💚</Text>
              </Animated.View>
              <Animated.View style={[styles.overlay, styles.nopeOverlay, nopeOpacity]}>
                <Text style={styles.nopeText}>NOPE ❌</Text>
              </Animated.View>
            </>
          )}
        </Animated.View>
      </GestureDetector>
    </View>
  );
};

const styles = StyleSheet.create({
  cardContainer: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'center',
    alignItems: 'center',
  },
  card: {
    width: SCREEN_WIDTH * 0.9,
    height: SCREEN_HEIGHT * 0.7,
    backgroundColor: '#1A1A2E',
    borderRadius: 20,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: '#2A2A4A',
    elevation: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.3,
    shadowRadius: 15,
  },
  skeleton: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: '#2A2A4A',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1,
  },
  imageContainer: {
    height: '55%',
    width: '100%',
    position: 'relative',
  },
  image: {
    width: '100%',
    height: '100%',
  },
  gradient: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    height: '40%',
  },
  badge: {
    position: 'absolute',
    top: 15,
    left: 15,
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderRadius: 20,
    zIndex: 2,
  },
  badgeText: {
    color: '#FFF',
    fontSize: 12,
    fontWeight: 'bold',
  },
  primeBadge: {
    position: 'absolute',
    top: 15,
    right: 15,
    backgroundColor: '#00A8E1',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 4,
    zIndex: 2,
  },
  primeText: {
    color: '#FFF',
    fontSize: 10,
    fontWeight: '900',
    fontStyle: 'italic',
  },
  discountCircle: {
    position: 'absolute',
    bottom: 10,
    left: 15,
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: '#00F5A0',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 3,
    borderColor: '#1A1A2E',
    zIndex: 3,
  },
  discountText: {
    color: '#0D0D1A',
    fontSize: 14,
    fontWeight: 'bold',
  },
  offText: {
    color: '#0D0D1A',
    fontSize: 10,
    fontWeight: 'bold',
  },
  content: {
    padding: 20,
    height: '45%',
    justifyContent: 'space-between',
  },
  title: {
    color: '#FFF',
    fontSize: 18,
    fontWeight: 'bold',
  },
  ratingRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 5,
  },
  stars: {
    flexDirection: 'row',
    marginRight: 8,
  },
  reviewCount: {
    color: '#A0A0B0',
    fontSize: 12,
  },
  priceRow: {
    flexDirection: 'row',
    alignItems: 'baseline',
  },
  originalPrice: {
    color: '#A0A0B0',
    fontSize: 14,
    textDecorationLine: 'line-through',
    marginRight: 10,
  },
  dealPrice: {
    color: '#00F5A0',
    fontSize: 24,
    fontWeight: 'bold',
  },
  timer: {
    color: '#FFD700',
    fontSize: 12,
    fontWeight: '600',
    marginTop: 5,
  },
  progressContainer: {
    marginTop: 10,
    height: 6,
    backgroundColor: '#2A2A4A',
    borderRadius: 3,
    position: 'relative',
  },
  progressBar: {
    height: '100%',
    backgroundColor: '#FF6B35',
    borderRadius: 3,
  },
  progressText: {
    position: 'absolute',
    top: 8,
    right: 0,
    color: '#A0A0B0',
    fontSize: 10,
  },
  cta: {
    marginTop: 15,
    borderRadius: 25,
    overflow: 'hidden',
  },
  ctaGradient: {
    paddingVertical: 12,
    alignItems: 'center',
  },
  ctaText: {
    color: '#FFF',
    fontWeight: 'bold',
    fontSize: 16,
  },
  overlay: {
    position: 'absolute',
    top: 40,
    borderWidth: 4,
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 10,
    zIndex: 100,
  },
  likeOverlay: {
    left: 20,
    borderColor: '#00F5A0',
    transform: [{ rotate: '-15deg' }],
  },
  nopeOverlay: {
    right: 20,
    borderColor: '#FF4757',
    transform: [{ rotate: '15deg' }],
  },
  likeText: {
    color: '#00F5A0',
    fontSize: 32,
    fontWeight: 'bold',
  },
  nopeText: {
    color: '#FF4757',
    fontSize: 32,
    fontWeight: 'bold',
  },
});
