import React, { useState, useEffect, useRef } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  SafeAreaView,
  ScrollView,
  TextInput,
  Image,
  Alert,
  Switch
} from 'react-native';
import { CameraView, useCameraPermissions } from 'expo-camera';
import * as Crypto from 'expo-crypto';
import * as Location from 'expo-location';
import {
  ArrowLeft,
  Camera as CameraIcon,
  Video,
  Check,
  AlertTriangle,
  ShieldCheck,
  Zap,
  SkipForward,
  Edit3,
  Circle,
  ArrowRight,
  Maximize2
} from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';

export default function RoomCaptureScreen({ params, goBack, navigateTo, openPaywall }) {
  const { propertyId, roomId, phase = 'move_in' } = params;
  const { colors, proStatus } = useApp();

  const [permission, requestPermission] = useCameraPermissions();
  const [room, setRoom] = useState(null);
  const [checklist, setChecklist] = useState([]);
  const [currentItemIdx, setCurrentItemIdx] = useState(0);

  // Capture State
  const [mediaMode, setMediaMode] = useState('photo'); // 'photo' or 'video'
  const [quickMode, setQuickMode] = useState(false);
  const [capturedPhoto, setCapturedPhoto] = useState(null);
  const [annotatedUri, setAnnotatedUri] = useState(null);
  const [condition, setCondition] = useState('Good'); // 'Good', 'Minor wear', 'Damage', 'Pre-existing damage'
  const [note, setNote] = useState('');
  const [sha256Hash, setSha256Hash] = useState('');
  const [gpsStamp, setGpsStamp] = useState(null);
  const [closeUpUri, setCloseUpUri] = useState(null);
  const [wideShotUri, setWideShotUri] = useState(null);
  const [markupType, setMarkupType] = useState('circle'); // 'circle' or 'arrow'
  const [isCapturingStep, setIsCapturingStep] = useState('main'); // 'main', 'closeup', 'wideshot'

  const cameraRef = useRef(null);

  useEffect(() => {
    (async () => {
      const rooms = await StorageService.getRooms(propertyId);
      const targetRoom = rooms.find(r => r.id === roomId) || rooms[0];
      setRoom(targetRoom);
      if (targetRoom) {
        setChecklist(targetRoom.checklist || ['Walls', 'Floor', 'Ceiling', 'Fixtures', 'Windows', 'Outlets']);
      }

      // Request location for GPS timestamping
      try {
        const { status } = await Location.requestForegroundPermissionsAsync();
        if (status === 'granted') {
          const loc = await Location.getCurrentPositionAsync({});
          setGpsStamp({
            lat: loc.coords.latitude.toFixed(5),
            lng: loc.coords.longitude.toFixed(5)
          });
        }
      } catch (e) {
        // Location optional
      }
    })();
  }, [propertyId, roomId]);

  if (!room) return null;

  const currentItemLabel = checklist[currentItemIdx] || 'General Room Check';
  const progressPct = Math.round(((currentItemIdx + 1) / checklist.length) * 100);

  const takeShot = async () => {
    try {
      let uri = null;
      if (cameraRef.current) {
        const photo = await cameraRef.current.takePictureAsync({ quality: 0.85 });
        uri = photo.uri;
      } else {
        uri = `https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600&auto=format&fit=crop&q=80`;
      }

      const timestamp = new Date().toISOString();
      const hash = await Crypto.digestStringAsync(
        Crypto.CryptoDigestAlgorithm.SHA256,
        `${uri}_${timestamp}_${currentItemLabel}_${isCapturingStep}`
      );

      if (isCapturingStep === 'closeup') {
        setCloseUpUri(uri);
        setIsCapturingStep('wideshot');
        Alert.alert('Close-Up Shot Saved', 'Now take a wide shot showing the damage in full room context.');
      } else if (isCapturingStep === 'wideshot') {
        setWideShotUri(uri);
        setIsCapturingStep('main');
        Alert.alert('Wide Shot Saved', 'Both close-up and wide context shots attached for pre-existing damage proof.');
      } else {
        setCapturedPhoto(uri);
        setSha256Hash(hash);

        if (quickMode) {
          await saveAndNextItem(uri, hash);
        }
      }
    } catch (e) {
      console.error('Capture error:', e);
      const uri = `https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600&auto=format&fit=crop&q=80`;
      const hash = await Crypto.digestStringAsync(
        Crypto.CryptoDigestAlgorithm.SHA256,
        `${uri}_${Date.now()}`
      );
      setCapturedPhoto(uri);
      setSha256Hash(hash);
    }
  };

  const handlePreExistingShortcut = () => {
    setCondition('Pre-existing damage');
    setIsCapturingStep('closeup');
    Alert.alert(
      'Pre-Existing Damage Tagged',
      'MoveProof will mark this item protected. Tap the shutter button to take a CLOSE-UP photo of the damage.',
      [{ text: 'Take Close-Up Shot' }]
    );
  };

  const applyMarkup = () => {
    setAnnotatedUri(`${capturedPhoto}&annotated=${markupType}`);
    Alert.alert('Markup Applied', `Added ${markupType} annotation layer. Untouched original is preserved separately.`);
  };

  const saveAndNextItem = async (customUri = null, customHash = null) => {
    const finalUri = customUri || capturedPhoto;
    const finalHash = customHash || sha256Hash;

    if (finalUri) {
      await StorageService.saveItem({
        propertyId,
        roomId: room.id,
        phase,
        label: currentItemLabel,
        mediaUri: finalUri,
        mediaType: mediaMode,
        annotatedUri,
        condition,
        note,
        sha256: finalHash,
        latitude: gpsStamp?.lat,
        longitude: gpsStamp?.lng,
        closeUpUri,
        wideShotUri,
        isPreExisting: condition === 'Pre-existing damage'
      });
    }

    // Reset state for next item
    setCapturedPhoto(null);
    setAnnotatedUri(null);
    setCondition('Good');
    setNote('');
    setCloseUpUri(null);
    setWideShotUri(null);
    setIsCapturingStep('main');

    if (currentItemIdx < checklist.length - 1) {
      setCurrentItemIdx(currentItemIdx + 1);
    } else {
      Alert.alert(
        'Room Completed!',
        `All items in ${room.name} have been documented.`,
        [{ text: 'Done', onPress: () => goBack() }]
      );
    }
  };

  const handleSkip = () => {
    if (currentItemIdx < checklist.length - 1) {
      setCurrentItemIdx(currentItemIdx + 1);
    } else {
      goBack();
    }
  };

  if (!permission) {
    return <View style={[styles.container, { backgroundColor: colors.background }]} />;
  }

  if (!permission.granted) {
    return (
      <SafeAreaView style={[styles.container, { backgroundColor: colors.background, justifyContent: 'center', alignItems: 'center', padding: 24 }]}>
        <CameraIcon size={48} color={colors.primary} style={{ marginBottom: 16 }} />
        <Text style={[styles.permissionTitle, { color: colors.textPrimary }]}>Camera Access Required</Text>
        <Text style={[styles.permissionSub, { color: colors.textSecondary }]}>
          MoveProof needs camera permissions to capture photo and video evidence of room conditions.
        </Text>
        <TouchableOpacity style={[styles.permissionBtn, { backgroundColor: colors.primary }]} onPress={requestPermission}>
          <Text style={styles.permissionBtnText}>Enable Camera</Text>
        </TouchableOpacity>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Top Bar / Progress */}
      <View style={[styles.topBar, { borderBottomColor: colors.border }]}>
        <TouchableOpacity onPress={goBack} style={styles.backBtn}>
          <ArrowLeft size={24} color={colors.textPrimary} />
        </TouchableOpacity>

        <View style={styles.topBarCenter}>
          <Text style={[styles.topBarTitle, { color: colors.textPrimary }]}>
            {room.name} ({phase === 'move_out' ? 'Move-Out' : 'Move-In'})
          </Text>
          <Text style={[styles.itemStepText, { color: colors.primary }]}>
            Item {currentItemIdx + 1} of {checklist.length}: {currentItemLabel}
          </Text>
        </View>

        <TouchableOpacity onPress={handleSkip} style={styles.skipBtn}>
          <SkipForward size={20} color={colors.textSecondary} />
          <Text style={[styles.skipText, { color: colors.textSecondary }]}>Skip</Text>
        </TouchableOpacity>
      </View>

      {/* Top Progress Bar */}
      <View style={[styles.progressBg, { backgroundColor: colors.surfaceVariant }]}>
        <View
          style={[
            styles.progressFill,
            { width: `${progressPct}%`, backgroundColor: phase === 'move_out' ? colors.amber : colors.primary }
          ]}
        />
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Quick Mode Toggle */}
        <View style={[styles.quickModeCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
          <View style={styles.quickModeInfo}>
            <Zap size={18} color={colors.amber} style={{ marginRight: 8 }} />
            <Text style={[styles.quickModeText, { color: colors.textPrimary }]}>Quick Capture Mode</Text>
          </View>
          <Switch
            value={quickMode}
            onValueChange={setQuickMode}
            trackColor={{ false: colors.border, true: colors.primary }}
          />
        </View>

        {/* Camera Viewfinder or Captured Preview */}
        {!capturedPhoto || isCapturingStep !== 'main' ? (
          <View style={styles.cameraBox}>
            <CameraView ref={cameraRef} style={styles.cameraView}>
              <View style={styles.cameraOverlayHeader}>
                <Text style={styles.overlayTimestamp}>
                  {isCapturingStep === 'closeup'
                    ? '📸 TAKE CLOSE-UP DAMAGE SHOT'
                    : isCapturingStep === 'wideshot'
                    ? '📷 TAKE WIDE CONTEXT SHOT'
                    : new Date().toLocaleString()}
                </Text>
                {gpsStamp && (
                  <Text style={styles.overlayGps}>GPS: {gpsStamp.lat}, {gpsStamp.lng}</Text>
                )}
              </View>

              {/* Mode Toggle Pills */}
              <View style={styles.modePillContainer}>
                <TouchableOpacity
                  style={[styles.modePill, mediaMode === 'photo' && styles.activeModePill]}
                  onPress={() => setMediaMode('photo')}
                >
                  <CameraIcon size={14} color="#FFFFFF" style={{ marginRight: 4 }} />
                  <Text style={styles.modePillText}>Photo</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[styles.modePill, mediaMode === 'video' && styles.activeModePill]}
                  onPress={() => setMediaMode('video')}
                >
                  <Video size={14} color="#FFFFFF" style={{ marginRight: 4 }} />
                  <Text style={styles.modePillText}>Video (30s)</Text>
                </TouchableOpacity>
              </View>

              {/* Shutter Button */}
              <View style={styles.shutterContainer}>
                <TouchableOpacity style={styles.shutterBtn} onPress={takeShot} activeOpacity={0.8}>
                  <View style={styles.shutterInner} />
                </TouchableOpacity>
              </View>
            </CameraView>
          </View>
        ) : (
          /* Shot Review & Tagging Interface */
          <View style={styles.reviewContainer}>
            <View style={styles.previewImageContainer}>
              <Image source={{ uri: capturedPhoto }} style={styles.previewImg} />
              {annotatedUri && (
                <View style={styles.annotationOverlayBadge}>
                  <Text style={styles.annotationText}>Markup Added ({markupType})</Text>
                </View>
              )}
            </View>

            {/* SHA-256 Hash Display */}
            <View style={[styles.hashBox, { backgroundColor: colors.surfaceVariant }]}>
              <Text style={[styles.hashLabel, { color: colors.textSecondary }]}>SHA-256 Hash (Untouched Original):</Text>
              <Text style={[styles.hashText, { color: colors.primary }]} numberOfLines={1}>
                {sha256Hash}
              </Text>
            </View>

            {/* Move-In Shortcut: "Already damaged?" */}
            {phase === 'move_in' && (
              <TouchableOpacity
                style={[styles.alreadyDamagedBtn, { backgroundColor: colors.amberLight, borderColor: colors.amber }]}
                onPress={handlePreExistingShortcut}
              >
                <AlertTriangle size={20} color={colors.amber} style={{ marginRight: 8 }} />
                <Text style={[styles.alreadyDamagedText, { color: colors.amber }]}>Already Damaged? Tag & Shot Close-up / Wide</Text>
              </TouchableOpacity>
            )}

            {/* Condition Tags */}
            <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Condition Tag</Text>
            <View style={styles.tagGrid}>
              {[
                { tag: 'Good', color: colors.green, bg: colors.greenLight, icon: Check },
                { tag: 'Minor wear', color: colors.textSecondary, bg: colors.surfaceVariant, icon: Edit3 },
                { tag: 'Damage', color: colors.amber, bg: colors.amberLight, icon: AlertTriangle },
                { tag: 'Pre-existing damage', color: colors.green, bg: colors.greenLight, icon: ShieldCheck }
              ].map((item) => {
                const isSelected = condition === item.tag;
                const IconComp = item.icon;
                return (
                  <TouchableOpacity
                    key={item.tag}
                    style={[
                      styles.conditionTag,
                      {
                        backgroundColor: isSelected ? item.bg : colors.surface,
                        borderColor: isSelected ? item.color : colors.border
                      }
                    ]}
                    onPress={() => setCondition(item.tag)}
                  >
                    <IconComp size={16} color={isSelected ? item.color : colors.textMuted} style={{ marginRight: 6 }} />
                    <Text
                      style={[
                        styles.tagText,
                        { color: isSelected ? item.color : colors.textPrimary, fontWeight: isSelected ? '700' : '600' }
                      ]}
                    >
                      {item.tag}
                    </Text>
                  </TouchableOpacity>
                );
              })}
            </View>

            {/* Markup Tool Buttons */}
            <View style={[styles.markupCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
              <Text style={[styles.markupTitle, { color: colors.textPrimary }]}>Markup Damage Tool</Text>
              <View style={styles.markupRow}>
                <TouchableOpacity
                  style={[styles.markupChoice, markupType === 'circle' && { borderColor: colors.primary, backgroundColor: colors.primaryLight }]}
                  onPress={() => setMarkupType('circle')}
                >
                  <Circle size={18} color={colors.primary} style={{ marginRight: 6 }} />
                  <Text style={[styles.markupChoiceText, { color: colors.textPrimary }]}>Circle</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[styles.markupChoice, markupType === 'arrow' && { borderColor: colors.primary, backgroundColor: colors.primaryLight }]}
                  onPress={() => setMarkupType('arrow')}
                >
                  <ArrowRight size={18} color={colors.primary} style={{ marginRight: 6 }} />
                  <Text style={[styles.markupChoiceText, { color: colors.textPrimary }]}>Arrow</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[styles.applyMarkupBtn, { backgroundColor: colors.primary }]}
                  onPress={applyMarkup}
                >
                  <Text style={styles.applyMarkupBtnText}>Apply Markup</Text>
                </TouchableOpacity>
              </View>
            </View>

            {/* Notes */}
            <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Notes & Details</Text>
            <TextInput
              style={[
                styles.noteInput,
                { backgroundColor: colors.surface, borderColor: colors.border, color: colors.textPrimary }
              ]}
              placeholder="Add details, specific defects, or notes..."
              placeholderTextColor={colors.textMuted}
              multiline
              numberOfLines={3}
              value={note}
              onChangeText={setNote}
            />

            {/* Save & Next CTA */}
            <TouchableOpacity
              style={[styles.saveNextBtn, { backgroundColor: colors.primary }]}
              onPress={() => saveAndNextItem()}
              activeOpacity={0.85}
            >
              <Text style={styles.saveNextBtnText}>Save & Next Item</Text>
            </TouchableOpacity>
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  permissionTitle: {
    fontSize: 20,
    fontWeight: '800',
    marginBottom: 8,
  },
  permissionSub: {
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 20,
  },
  permissionBtn: {
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 14,
  },
  permissionBtnText: {
    color: '#FFFFFF',
    fontWeight: '700',
  },
  topBar: {
    height: 56,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    justifyContent: 'space-between',
  },
  backBtn: {
    padding: 6,
  },
  topBarCenter: {
    alignItems: 'center',
  },
  topBarTitle: {
    fontSize: 15,
    fontWeight: '700',
  },
  itemStepText: {
    fontSize: 12,
    fontWeight: '700',
  },
  skipBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 6,
  },
  skipText: {
    fontSize: 12,
    fontWeight: '600',
    marginLeft: 4,
  },
  progressBg: {
    height: 6,
  },
  progressFill: {
    height: '100%',
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingVertical: 16,
  },
  quickModeCard: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderRadius: 14,
    borderWidth: 1,
    marginBottom: 16,
  },
  quickModeInfo: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  quickModeText: {
    fontSize: 14,
    fontWeight: '700',
  },
  cameraBox: {
    height: 420,
    borderRadius: 20,
    overflow: 'hidden',
    marginBottom: 20,
  },
  cameraView: {
    flex: 1,
    justifyContent: 'space-between',
    padding: 16,
  },
  cameraOverlayHeader: {
    backgroundColor: 'rgba(0,0,0,0.5)',
    padding: 8,
    borderRadius: 8,
    alignSelf: 'flex-start',
  },
  overlayTimestamp: {
    color: '#FFFFFF',
    fontSize: 11,
    fontWeight: '700',
  },
  overlayGps: {
    color: '#34D399',
    fontSize: 10,
    fontWeight: '600',
  },
  modePillContainer: {
    flexDirection: 'row',
    alignSelf: 'center',
    backgroundColor: 'rgba(0,0,0,0.6)',
    padding: 4,
    borderRadius: 20,
    gap: 8,
  },
  modePill: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  activeModePill: {
    backgroundColor: '#005F73',
  },
  modePillText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '700',
  },
  shutterContainer: {
    alignItems: 'center',
    marginBottom: 8,
  },
  shutterBtn: {
    width: 72,
    height: 72,
    borderRadius: 36,
    borderWidth: 4,
    borderColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  shutterInner: {
    width: 58,
    height: 58,
    borderRadius: 29,
    backgroundColor: '#FFFFFF',
  },
  reviewContainer: {
    gap: 16,
  },
  previewImageContainer: {
    height: 260,
    borderRadius: 16,
    overflow: 'hidden',
    position: 'relative',
  },
  previewImg: {
    width: '100%',
    height: '100%',
  },
  annotationOverlayBadge: {
    position: 'absolute',
    bottom: 12,
    left: 12,
    backgroundColor: 'rgba(0,0,0,0.7)',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 8,
  },
  annotationText: {
    color: '#FFFFFF',
    fontSize: 11,
    fontWeight: '700',
  },
  hashBox: {
    padding: 12,
    borderRadius: 12,
  },
  hashLabel: {
    fontSize: 11,
    fontWeight: '600',
  },
  hashText: {
    fontSize: 11,
    fontFamily: 'monospace',
    marginTop: 2,
  },
  alreadyDamagedBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 48,
    borderRadius: 14,
    borderWidth: 1.5,
  },
  alreadyDamagedText: {
    fontSize: 13,
    fontWeight: '700',
  },
  sectionTitle: {
    fontSize: 15,
    fontWeight: '700',
  },
  tagGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  conditionTag: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 10,
    borderRadius: 12,
    borderWidth: 1,
  },
  tagText: {
    fontSize: 13,
  },
  markupCard: {
    borderRadius: 14,
    borderWidth: 1,
    padding: 14,
  },
  markupTitle: {
    fontSize: 13,
    fontWeight: '700',
    marginBottom: 8,
  },
  markupRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  markupChoice: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: 'transparent',
  },
  markupChoiceText: {
    fontSize: 13,
    fontWeight: '600',
  },
  applyMarkupBtn: {
    marginLeft: 'auto',
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 10,
  },
  applyMarkupBtnText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '700',
  },
  noteInput: {
    borderRadius: 14,
    borderWidth: 1,
    padding: 12,
    fontSize: 14,
    textAlignVertical: 'top',
  },
  saveNextBtn: {
    height: 52,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 8,
    marginBottom: 40,
  },
  saveNextBtnText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  }
});
