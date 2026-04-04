import {
  useSharedValue,
  useAnimatedStyle,
  withSpring,
  withTiming,
  runOnJS,
  interpolate,
  Extrapolation,
  SharedValue
} from 'react-native-reanimated';
import {
  Gesture,
  PanGesture
} from 'react-native-gesture-handler';
import { Dimensions } from 'react-native';
import * as Haptics from 'expo-haptics';

const { width: SCREEN_WIDTH } = Dimensions.get('window');
const SWIPE_THRESHOLD = 120;

interface UseSwipeProps {
  onSwipeLeft: () => void;
  onSwipeRight: () => void;
}

export interface SwipeHookResult {
  gesture: PanGesture;
  cardStyle: any;
  likeOpacity: any;
  nopeOpacity: any;
  translateX: SharedValue<number>;
}

export const useSwipe = ({ onSwipeLeft, onSwipeRight }: UseSwipeProps): SwipeHookResult => {
  const translateX = useSharedValue(0);
  const translateY = useSharedValue(0);

  const gesture = Gesture.Pan()
    .onUpdate((event) => {
      translateX.value = event.translationX;
      translateY.value = event.translationY;
    })
    .onEnd((event) => {
      if (Math.abs(event.translationX) > SWIPE_THRESHOLD) {
        if (event.translationX > 0) {
          translateX.value = withTiming(SCREEN_WIDTH * 1.5, { duration: 300 }, () => {
            runOnJS(Haptics.notificationAsync)(Haptics.NotificationFeedbackType.Success);
            runOnJS(onSwipeRight)();
            translateX.value = 0;
            translateY.value = 0;
          });
        } else {
          translateX.value = withTiming(-SCREEN_WIDTH * 1.5, { duration: 300 }, () => {
            runOnJS(Haptics.impactAsync)(Haptics.ImpactFeedbackStyle.Light);
            runOnJS(onSwipeLeft)();
            translateX.value = 0;
            translateY.value = 0;
          });
        }
      } else {
        translateX.value = withSpring(0);
        translateY.value = withSpring(0);
      }
    });

  const cardStyle = useAnimatedStyle(() => {
    const rotation = interpolate(
      translateX.value,
      [-SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2],
      [-10, 0, 10],
      Extrapolation.CLAMP
    );

    return {
      transform: [
        { translateX: translateX.value },
        { translateY: translateY.value },
        { rotate: `${rotation}deg` },
      ],
    };
  });

  const likeOpacity = useAnimatedStyle(() => ({
    opacity: interpolate(
      translateX.value,
      [0, SWIPE_THRESHOLD],
      [0, 1],
      Extrapolation.CLAMP
    ),
  }));

  const nopeOpacity = useAnimatedStyle(() => ({
    opacity: interpolate(
      translateX.value,
      [-SWIPE_THRESHOLD, 0],
      [1, 0],
      Extrapolation.CLAMP
    ),
  }));

  return { gesture, cardStyle, likeOpacity, nopeOpacity, translateX };
};
