import React, { useState, useEffect } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  SafeAreaView,
  ScrollView,
  Image,
  Dimensions
} from 'react-native';
import {
  ArrowLeft,
  ShieldCheck,
  AlertTriangle,
  CheckCircle2,
  HelpCircle,
  Sparkles,
  ChevronRight,
  Maximize2
} from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';

const { width } = Dimensions.get('window');

export default function CompareViewScreen({ params, goBack, navigateTo, openPaywall }) {
  const { propertyId } = params;
  const { colors, proStatus } = useApp();

  const [property, setProperty] = useState(null);
  const [rooms, setRooms] = useState([]);
  const [moveInItems, setMoveInItems] = useState([]);
  const [moveOutItems, setMoveOutItems] = useState([]);
  const [comparisonList, setComparisonList] = useState([]);

  const [protectedCount, setProtectedCount] = useState(0);
  const [newDamageCount, setNewDamageCount] = useState(0);
  const [notDocumentedCount, setNotDocumentedCount] = useState(0);

  useEffect(() => {
    (async () => {
      const props = await StorageService.getProperties();
      const prop = props.find(p => p.id === propertyId);
      setProperty(prop);

      const loadedRooms = await StorageService.getRooms(propertyId);
      setRooms(loadedRooms);

      const allItems = await StorageService.getItems(propertyId);
      const moveIn = allItems.filter(i => i.phase === 'move_in');
      const moveOut = allItems.filter(i => i.phase === 'move_out');

      setMoveInItems(moveIn);
      setMoveOutItems(moveOut);

      // Build consolidated item-by-item comparison list across rooms
      let protectedCnt = 0;
      let newDamageCnt = 0;
      let notDocCnt = 0;

      const compList = [];

      loadedRooms.forEach(room => {
        const roomChecklist = room.checklist || ['Walls', 'Floor', 'Ceiling', 'Fixtures', 'Windows', 'Outlets'];

        roomChecklist.forEach(label => {
          const moveInMatch = moveIn.find(i => i.roomId === room.id && i.label === label);
          const moveOutMatch = moveOut.find(i => i.roomId === room.id && i.label === label);

          let status = 'UNCHANGED'; // 'PROTECTED', 'NEW_DAMAGE', 'NOT_DOCUMENTED', 'UNCHANGED'

          if (moveInMatch?.isPreExisting || moveInMatch?.condition === 'Pre-existing damage') {
            status = 'PROTECTED';
            protectedCnt++;
          } else if (moveOutMatch && (moveOutMatch.condition === 'Damage' || moveOutMatch.condition === 'Minor wear') && (!moveInMatch || moveInMatch.condition === 'Good')) {
            status = 'NEW_DAMAGE';
            newDamageCnt++;
          } else if (moveOutMatch && !moveInMatch) {
            status = 'NOT_DOCUMENTED';
            notDocCnt++;
          }

          if (moveInMatch || moveOutMatch) {
            compList.push({
              id: `${room.id}_${label}`,
              roomName: room.name,
              label,
              moveInItem: moveInMatch,
              moveOutItem: moveOutMatch,
              status
            });
          }
        });
      });

      setProtectedCount(protectedCnt);
      setNewDamageCount(newDamageCnt);
      setNotDocumentedCount(notDocCnt);
      setComparisonList(compList);
    })();
  }, [propertyId]);

  if (!property) return null;

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Header */}
      <View style={[styles.header, { borderBottomColor: colors.border }]}>
        <TouchableOpacity onPress={goBack} style={styles.backBtn}>
          <ArrowLeft size={24} color={colors.textPrimary} />
        </TouchableOpacity>
        <View style={styles.headerTitleBox}>
          <Text style={[styles.headerTitle, { color: colors.textPrimary }]}>Compare View</Text>
          <Text style={[styles.headerSub, { color: colors.textSecondary }]}>
            {property.nickname || property.address}
          </Text>
        </View>
        <View style={[styles.heroBadge, { backgroundColor: colors.primaryLight }]}>
          <ShieldCheck size={16} color={colors.primary} />
        </View>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* HERO SUMMARY CARD */}
        <View style={[styles.summaryCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
          <Text style={[styles.summaryTitle, { color: colors.textPrimary }]}>
            Pre-Existing Damage Protection Summary
          </Text>

          <View style={styles.statsRow}>
            {/* Protected */}
            <View style={[styles.statBox, { backgroundColor: colors.greenLight }]}>
              <ShieldCheck size={22} color={colors.green} />
              <Text style={[styles.statNumber, { color: colors.green }]}>{protectedCount}</Text>
              <Text style={[styles.statLabel, { color: colors.green }]}>Protected Items</Text>
            </View>

            {/* New Damage */}
            <View style={[styles.statBox, { backgroundColor: colors.amberLight }]}>
              <AlertTriangle size={22} color={colors.amber} />
              <Text style={[styles.statNumber, { color: colors.amber }]}>{newDamageCount}</Text>
              <Text style={[styles.statLabel, { color: colors.amber }]}>New Damage</Text>
            </View>

            {/* Not Documented */}
            <View style={[styles.statBox, { backgroundColor: colors.surfaceVariant }]}>
              <HelpCircle size={22} color={colors.textSecondary} />
              <Text style={[styles.statNumber, { color: colors.textSecondary }]}>{notDocumentedCount}</Text>
              <Text style={[styles.statLabel, { color: colors.textSecondary }]}>Not Doc at Move-In</Text>
            </View>
          </View>

          {/* Gentle Warning Note for Riskiest Charges */}
          {notDocumentedCount > 0 && (
            <View style={[styles.warningBox, { backgroundColor: colors.amberLight }]}>
              <AlertTriangle size={16} color={colors.amber} style={{ marginRight: 6 }} />
              <Text style={[styles.warningText, { color: colors.amber }]}>
                {notDocumentedCount} item(s) were captured at move-out but lack a move-in baseline photo. These pose the highest risk for deposit deductions.
              </Text>
            </View>
          )}
        </View>

        {/* COMPARISON LIST */}
        <Text style={[styles.sectionHeader, { color: colors.textPrimary }]}>
          Side-By-Side Evidence Comparison
        </Text>

        {comparisonList.length === 0 ? (
          <View style={styles.emptyCompState}>
            <Text style={[styles.emptyCompText, { color: colors.textSecondary }]}>
              No photos captured yet. Document items during Move-In and Move-Out to enable live compare.
            </Text>
          </View>
        ) : (
          comparisonList.map((comp) => {
            return (
              <View
                key={comp.id}
                style={[styles.compCard, { backgroundColor: colors.surface, borderColor: colors.border }]}
              >
                {/* Item & Room Banner Header */}
                <View style={styles.compCardHeader}>
                  <Text style={[styles.compRoomText, { color: colors.textSecondary }]}>{comp.roomName}</Text>
                  <Text style={[styles.compLabelText, { color: colors.textPrimary }]}>{comp.label}</Text>
                </View>

                {/* Status Protection Banners */}
                {comp.status === 'PROTECTED' && (
                  <View style={[styles.statusBanner, { backgroundColor: colors.greenLight }]}>
                    <ShieldCheck size={18} color={colors.green} style={{ marginRight: 6 }} />
                    <Text style={[styles.statusBannerText, { color: colors.green }]}>
                      Protected: Documented at move-in as pre-existing damage
                    </Text>
                  </View>
                )}

                {comp.status === 'NEW_DAMAGE' && (
                  <View style={[styles.statusBanner, { backgroundColor: colors.amberLight }]}>
                    <AlertTriangle size={18} color={colors.amber} style={{ marginRight: 6 }} />
                    <Text style={[styles.statusBannerText, { color: colors.amber }]}>
                      New Damage Claimed at Move-Out
                    </Text>
                  </View>
                )}

                {comp.status === 'NOT_DOCUMENTED' && (
                  <View style={[styles.statusBanner, { backgroundColor: colors.surfaceVariant }]}>
                    <HelpCircle size={18} color={colors.textSecondary} style={{ marginRight: 6 }} />
                    <Text style={[styles.statusBannerText, { color: colors.textSecondary }]}>
                      Not Documented at Move-In (High Deposit Risk)
                    </Text>
                  </View>
                )}

                {comp.status === 'UNCHANGED' && (
                  <View style={[styles.statusBanner, { backgroundColor: colors.greenLight }]}>
                    <CheckCircle2 size={18} color={colors.green} style={{ marginRight: 6 }} />
                    <Text style={[styles.statusBannerText, { color: colors.green }]}>
                      Unchanged / Normal Condition
                    </Text>
                  </View>
                )}

                {/* Side-by-side Photo Comparison Container */}
                <View style={styles.photosSideBySide}>
                  {/* Move-In Photo */}
                  <View style={styles.photoCol}>
                    <Text style={[styles.photoColLabel, { color: colors.textSecondary }]}>Move-In Baseline</Text>
                    <View style={[styles.photoFrame, { backgroundColor: colors.surfaceVariant }]}>
                      {comp.moveInItem?.mediaUri ? (
                        <Image source={{ uri: comp.moveInItem.mediaUri }} style={styles.photoImg} />
                      ) : (
                        <View style={styles.noPhotoBox}>
                          <Text style={[styles.noPhotoText, { color: colors.textMuted }]}>No Move-In Photo</Text>
                        </View>
                      )}
                    </View>
                    {comp.moveInItem && (
                      <Text style={[styles.hashFooter, { color: colors.textMuted }]} numberOfLines={1}>
                        SHA-256: {comp.moveInItem.sha256?.substring(0, 16)}...
                      </Text>
                    )}
                  </View>

                  {/* Move-Out Photo */}
                  <View style={styles.photoCol}>
                    <Text style={[styles.photoColLabel, { color: colors.textSecondary }]}>Move-Out Condition</Text>
                    <View style={[styles.photoFrame, { backgroundColor: colors.surfaceVariant }]}>
                      {comp.moveOutItem?.mediaUri ? (
                        <Image source={{ uri: comp.moveOutItem.mediaUri }} style={styles.photoImg} />
                      ) : (
                        <View style={styles.noPhotoBox}>
                          <Text style={[styles.noPhotoText, { color: colors.textMuted }]}>No Move-Out Photo</Text>
                        </View>
                      )}
                    </View>
                    {comp.moveOutItem && (
                      <Text style={[styles.hashFooter, { color: colors.textMuted }]} numberOfLines={1}>
                        SHA-256: {comp.moveOutItem.sha256?.substring(0, 16)}...
                      </Text>
                    )}
                  </View>
                </View>
              </View>
            );
          })
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    height: 60,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    borderBottomWidth: 1,
  },
  backBtn: {
    padding: 6,
    marginRight: 8,
  },
  headerTitleBox: {
    flex: 1,
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: '800',
  },
  headerSub: {
    fontSize: 12,
  },
  heroBadge: {
    padding: 8,
    borderRadius: 20,
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingVertical: 16,
  },
  summaryCard: {
    borderRadius: 18,
    borderWidth: 1,
    padding: 16,
    marginBottom: 20,
  },
  summaryTitle: {
    fontSize: 16,
    fontWeight: '800',
    marginBottom: 14,
  },
  statsRow: {
    flexDirection: 'row',
    gap: 8,
    marginBottom: 12,
  },
  statBox: {
    flex: 1,
    padding: 10,
    borderRadius: 12,
    alignItems: 'center',
  },
  statNumber: {
    fontSize: 20,
    fontWeight: '800',
    marginVertical: 4,
  },
  statLabel: {
    fontSize: 10,
    fontWeight: '700',
    textAlign: 'center',
  },
  warningBox: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 10,
    borderRadius: 10,
  },
  warningText: {
    fontSize: 11,
    fontWeight: '600',
    flex: 1,
  },
  sectionHeader: {
    fontSize: 17,
    fontWeight: '800',
    marginBottom: 12,
  },
  emptyCompState: {
    padding: 24,
    alignItems: 'center',
  },
  emptyCompText: {
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
  },
  compCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 14,
    marginBottom: 16,
  },
  compCardHeader: {
    marginBottom: 8,
  },
  compRoomText: {
    fontSize: 12,
    fontWeight: '600',
    textTransform: 'uppercase',
  },
  compLabelText: {
    fontSize: 16,
    fontWeight: '800',
  },
  statusBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 10,
    marginBottom: 12,
  },
  statusBannerText: {
    fontSize: 12,
    fontWeight: '700',
    flex: 1,
  },
  photosSideBySide: {
    flexDirection: 'row',
    gap: 12,
  },
  photoCol: {
    flex: 1,
  },
  photoColLabel: {
    fontSize: 11,
    fontWeight: '600',
    marginBottom: 4,
  },
  photoFrame: {
    height: 120,
    borderRadius: 12,
    overflow: 'hidden',
    justifyContent: 'center',
    alignItems: 'center',
  },
  photoImg: {
    width: '100%',
    height: '100%',
  },
  noPhotoBox: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  noPhotoText: {
    fontSize: 11,
    fontWeight: '600',
  },
  hashFooter: {
    fontSize: 9,
    fontFamily: 'monospace',
    marginTop: 4,
  }
});
