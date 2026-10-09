import React, { useState, useEffect } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  SafeAreaView,
  FlatList,
  Alert,
  TextInput,
  Switch
} from 'react-native';
import {
  ArrowLeft,
  MoreVertical,
  CheckCircle2,
  Camera,
  Calendar,
  DollarSign,
  Share2,
  FileText,
  Shield,
  Trash2,
  Scale,
  Sparkles,
  Edit3,
  Link2,
  Send,
  CheckSquare
} from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';
import { getStateRule } from '../config/depositRules';
import { PdfReportGenerator } from '../services/PdfReportGenerator';
import LandlordVerifyLinkModal from '../components/LandlordVerifyLinkModal';

export default function PropertyDetailScreen({ params, goBack, navigateTo, openPaywall }) {
  const { propertyId } = params;
  const { colors, refreshProperties, proStatus } = useApp();

  const [property, setProperty] = useState(null);
  const [rooms, setRooms] = useState([]);
  const [items, setItems] = useState([]);
  const [depositLog, setDepositLog] = useState({});
  const [activeTab, setActiveTab] = useState('Move-In'); // 'Move-In', 'Move-Out', 'Deposit Tools'
  const [menuOpen, setMenuOpen] = useState(false);
  const [verifyModalVisible, setVerifyModalVisible] = useState(false);

  // Deposit Log Inputs
  const [forwardingSentDate, setForwardingSentDate] = useState('');
  const [depositReceivedDate, setDepositReceivedDate] = useState('');
  const [amountReturned, setAmountReturned] = useState('');
  const [itemizedListReceived, setItemizedListReceived] = useState(false);

  const loadPropertyData = async () => {
    const props = await StorageService.getProperties();
    const prop = props.find(p => p.id === propertyId);
    setProperty(prop);

    const loadedRooms = await StorageService.getRooms(propertyId);
    setRooms(loadedRooms);

    const loadedItems = await StorageService.getItems(propertyId);
    setItems(loadedItems);

    const log = await StorageService.getDepositLog(propertyId);
    if (log) {
      setDepositLog(log);
      setForwardingSentDate(log.forwardingAddressSentDate || '');
      setDepositReceivedDate(log.depositReceivedDate || '');
      setAmountReturned(log.amountReturned ? log.amountReturned.toString() : '');
      setItemizedListReceived(!!log.itemizedListReceived);
    }
  };

  useEffect(() => {
    loadPropertyData();
  }, [propertyId]);

  if (!property) return null;

  const stateRule = getStateRule(property.region);

  const handleSaveDepositLog = async () => {
    const updated = await StorageService.saveDepositLog(propertyId, {
      forwardingAddressSentDate: forwardingSentDate,
      depositReceivedDate,
      amountReturned: amountReturned ? parseFloat(amountReturned) : 0,
      itemizedListReceived
    });
    setDepositLog(updated);
    Alert.alert('Deposit Log Saved', 'Updated deposit timeline log.');
  };

  const handleExportPdfReport = async () => {
    if (!proStatus.isPro && items.length > 15) {
      openPaywall();
      return;
    }
    try {
      await PdfReportGenerator.generatePropertyReport({
        property,
        rooms,
        items,
        isPro: proStatus.isPro
      });
    } catch (e) {
      Alert.alert('Export Error', 'Could not generate PDF report.');
    }
  };

  const handleDeleteProperty = () => {
    Alert.alert(
      'Delete Property',
      'Are you sure you want to delete this property and all associated photos and evidence? This cannot be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: async () => {
            await StorageService.deleteProperty(propertyId);
            await refreshProperties();
            goBack();
          }
        }
      ]
    );
  };

  // Calculate completion percentage per room
  const currentPhase = activeTab === 'Move-Out' ? 'move_out' : 'move_in';

  const getRoomStats = (room) => {
    const roomItems = items.filter(i => i.roomId === room.id && i.phase === currentPhase);
    const totalItems = room.checklist?.length || 6;
    const completedCount = roomItems.length;
    const pct = Math.min(100, Math.round((completedCount / totalItems) * 100));
    return { completedCount, totalItems, pct };
  };

  const renderRoomItem = ({ item }) => {
    const { completedCount, totalItems, pct } = getRoomStats(item);

    return (
      <TouchableOpacity
        style={[styles.roomCard, { backgroundColor: colors.surface, borderColor: colors.border }]}
        onPress={() => navigateTo('ROOM_CAPTURE', { propertyId, roomId: item.id, phase: currentPhase })}
        activeOpacity={0.8}
      >
        <View style={styles.roomHeader}>
          <Text style={[styles.roomTitle, { color: colors.textPrimary }]}>{item.name}</Text>
          <View style={[styles.completionRingPill, { backgroundColor: pct === 100 ? colors.greenLight : colors.surfaceVariant }]}>
            <Text style={[styles.completionText, { color: pct === 100 ? colors.green : colors.textSecondary }]}>
              {completedCount}/{totalItems} items ({pct}%)
            </Text>
          </View>
        </View>

        <View style={[styles.progressBg, { backgroundColor: colors.surfaceVariant }]}>
          <View
            style={[
              styles.progressFill,
              { width: `${pct}%`, backgroundColor: currentPhase === 'move_out' ? colors.amber : colors.primary }
            ]}
          />
        </View>

        <View style={styles.roomFooter}>
          <Text style={[styles.tapToCaptureText, { color: colors.primary }]}>Tap to open camera & checklist</Text>
          <Camera size={18} color={colors.primary} />
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Top Header */}
      <View style={[styles.header, { borderBottomColor: colors.border }]}>
        <TouchableOpacity onPress={goBack} style={styles.backBtn}>
          <ArrowLeft size={24} color={colors.textPrimary} />
        </TouchableOpacity>
        <View style={styles.headerInfo}>
          <Text style={[styles.headerTitle, { color: colors.textPrimary }]} numberOfLines={1}>
            {property.nickname || property.address}
          </Text>
          <Text style={[styles.headerSub, { color: colors.textSecondary }]} numberOfLines={1}>
            {property.address}
          </Text>
        </View>
        <TouchableOpacity onPress={() => setMenuOpen(!menuOpen)} style={styles.menuBtn}>
          <MoreVertical size={24} color={colors.textPrimary} />
        </TouchableOpacity>
      </View>

      {/* Menu Dropdown */}
      {menuOpen && (
        <View style={[styles.menuDropdown, { backgroundColor: colors.surface, borderColor: colors.border }]}>
          <TouchableOpacity
            style={styles.menuItem}
            onPress={() => {
              setMenuOpen(false);
              handleExportPdfReport();
            }}
          >
            <FileText size={18} color={colors.textPrimary} style={{ marginRight: 10 }} />
            <Text style={[styles.menuText, { color: colors.textPrimary }]}>Export Report PDF</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.menuItem}
            onPress={() => {
              setMenuOpen(false);
              setVerifyModalVisible(true);
            }}
          >
            <Link2 size={18} color={colors.primary} style={{ marginRight: 10 }} />
            <Text style={[styles.menuText, { color: colors.primary }]}>Share Verify Link</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.menuItem}
            onPress={() => {
              setMenuOpen(false);
              navigateTo('COMPARE_VIEW', { propertyId });
            }}
          >
            <Shield size={18} color={colors.textPrimary} style={{ marginRight: 10 }} />
            <Text style={[styles.menuText, { color: colors.textPrimary }]}>Compare View</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.menuItem}
            onPress={() => {
              setMenuOpen(false);
              handleDeleteProperty();
            }}
          >
            <Trash2 size={18} color={colors.danger} style={{ marginRight: 10 }} />
            <Text style={[styles.menuText, { color: colors.danger }]}>Delete Property</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* Tabs */}
      <View style={[styles.tabsContainer, { borderBottomColor: colors.border }]}>
        {['Move-In', 'Move-Out', 'Deposit Tools'].map((tab) => {
          const isSelected = activeTab === tab;
          return (
            <TouchableOpacity
              key={tab}
              style={[
                styles.tab,
                isSelected && { borderBottomColor: colors.primary, borderBottomWidth: 3 }
              ]}
              onPress={() => setActiveTab(tab)}
            >
              <Text
                style={[
                  styles.tabText,
                  { color: isSelected ? colors.primary : colors.textSecondary, fontWeight: isSelected ? '700' : '600' }
                ]}
              >
                {tab}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>

      {/* Main Tab Content */}
      {activeTab === 'Deposit Tools' ? (
        <ScrollView contentContainerStyle={styles.depositToolsContent} showsVerticalScrollIndicator={false}>
          {/* Quick Actions Bar */}
          <View style={styles.quickActionsRow}>
            <TouchableOpacity
              style={[styles.quickActionBtn, { backgroundColor: colors.primary }]}
              onPress={handleExportPdfReport}
            >
              <FileText size={18} color="#FFFFFF" />
              <Text style={styles.quickActionText}>Export PDF</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.quickActionBtn, { backgroundColor: colors.surface, borderColor: colors.primary, borderWidth: 1 }]}
              onPress={() => setVerifyModalVisible(true)}
            >
              <Link2 size={18} color={colors.primary} />
              <Text style={[styles.quickActionText, { color: colors.primary }]}>Verify Link</Text>
            </TouchableOpacity>
          </View>

          {/* Compare Hero Card */}
          <TouchableOpacity
            style={[styles.compareHeroCard, { backgroundColor: colors.primaryLight, borderColor: colors.primary }]}
            onPress={() => navigateTo('COMPARE_VIEW', { propertyId })}
            activeOpacity={0.85}
          >
            <View style={styles.compareHeroHeader}>
              <Shield size={24} color={colors.primary} />
              <View style={[styles.heroPill, { backgroundColor: colors.primary }]}>
                <Text style={styles.heroPillText}>HERO FEATURE</Text>
              </View>
            </View>
            <Text style={[styles.compareTitle, { color: colors.primaryDark }]}>
              Pre-Existing Damage Compare View
            </Text>
            <Text style={[styles.compareSub, { color: colors.textSecondary }]}>
              Side-by-side comparison of Move-In vs Move-Out photos. Automatically flags pre-existing damage protected items with green banners.
            </Text>
          </TouchableOpacity>

          {/* Deposit Return Deadline & Log Tracking */}
          <View style={[styles.toolCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <View style={styles.toolHeader}>
              <Calendar size={22} color={colors.amber} style={{ marginRight: 8 }} />
              <Text style={[styles.toolTitle, { color: colors.textPrimary }]}>Deposit Deadline & Log</Text>
            </View>

            <View style={[styles.deadlinePill, { backgroundColor: colors.amberLight }]}>
              <Text style={[styles.deadlineDaysText, { color: colors.amber }]}>
                {property.moveOutDate ? `${stateRule.daysAllowed}-Day Statutory Return Window` : 'Set Move-Out Date to Enable Countdown'}
              </Text>
            </View>

            {/* Deposit Log Form */}
            <Text style={[styles.logSectionTitle, { color: colors.textPrimary }]}>Deposit Activity Log</Text>

            <Text style={[styles.label, { color: colors.textSecondary }]}>Forwarding Address Sent Date</Text>
            <TextInput
              style={[styles.input, { backgroundColor: colors.background, borderColor: colors.border, color: colors.textPrimary }]}
              placeholder="YYYY-MM-DD"
              placeholderTextColor={colors.textMuted}
              value={forwardingSentDate}
              onChangeText={setForwardingSentDate}
            />

            <Text style={[styles.label, { color: colors.textSecondary }]}>Deposit Return Received Date</Text>
            <TextInput
              style={[styles.input, { backgroundColor: colors.background, borderColor: colors.border, color: colors.textPrimary }]}
              placeholder="YYYY-MM-DD"
              placeholderTextColor={colors.textMuted}
              value={depositReceivedDate}
              onChangeText={setDepositReceivedDate}
            />

            <Text style={[styles.label, { color: colors.textSecondary }]}>Amount Returned ($)</Text>
            <TextInput
              style={[styles.input, { backgroundColor: colors.background, borderColor: colors.border, color: colors.textPrimary }]}
              placeholder="0.00"
              placeholderTextColor={colors.textMuted}
              keyboardType="numeric"
              value={amountReturned}
              onChangeText={setAmountReturned}
            />

            <View style={styles.switchRow}>
              <Text style={[styles.label, { color: colors.textPrimary, flex: 1 }]}>Itemized Deductions Received?</Text>
              <Switch
                value={itemizedListReceived}
                onValueChange={setItemizedListReceived}
                trackColor={{ false: colors.border, true: colors.primary }}
              />
            </View>

            <TouchableOpacity style={[styles.saveLogBtn, { backgroundColor: colors.primary }]} onPress={handleSaveDepositLog}>
              <Text style={styles.saveLogBtnText}>Save Deposit Log</Text>
            </TouchableOpacity>

            <View style={[styles.penaltyBox, { backgroundColor: colors.surfaceVariant }]}>
              <Text style={[styles.penaltyLabel, { color: colors.textPrimary }]}>Local Penalty Note:</Text>
              <Text style={[styles.penaltyNote, { color: colors.textSecondary }]}>{stateRule.penaltyNote}</Text>
              <Text style={[styles.verifyNote, { color: colors.amber }]}>
                Rules vary by local ordinance. Confirm with your local housing authority. Last verified: {stateRule.lastReviewed}
              </Text>
            </View>
          </View>

          {/* Demand Letter Generator */}
          <View style={[styles.toolCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <View style={styles.toolHeader}>
              <Scale size={22} color={colors.primary} style={{ marginRight: 8 }} />
              <Text style={[styles.toolTitle, { color: colors.textPrimary }]}>Demand Letter Generator</Text>
              {!proStatus.isPro && (
                <View style={[styles.proBadge, { backgroundColor: colors.amberLight }]}>
                  <Sparkles size={12} color={colors.amber} style={{ marginRight: 4 }} />
                  <Text style={[styles.proBadgeText, { color: colors.amber }]}>PRO</Text>
                </View>
              )}
            </View>

            <Text style={[styles.toolDesc, { color: colors.textSecondary }]}>
              Automatically generates a legal demand letter referencing state statutes and pre-existing damage evidence.
            </Text>

            <TouchableOpacity
              style={[styles.generateBtn, { backgroundColor: colors.primary }]}
              onPress={() => {
                if (!proStatus.isPro) {
                  openPaywall();
                } else {
                  navigateTo('DEMAND_LETTER', { propertyId });
                }
              }}
            >
              <FileText size={18} color="#FFFFFF" style={{ marginRight: 8 }} />
              <Text style={styles.generateBtnText}>Generate Demand Letter</Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      ) : (
        <FlatList
          data={rooms}
          keyExtractor={(item) => item.id}
          renderItem={renderRoomItem}
          contentContainerStyle={styles.listContent}
          showsVerticalScrollIndicator={false}
        />
      )}

      {/* Verification Link Modal */}
      <LandlordVerifyLinkModal
        visible={verifyModalVisible}
        onClose={() => setVerifyModalVisible(false)}
        propertyId={propertyId}
        openPaywall={openPaywall}
      />
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
  headerInfo: {
    flex: 1,
  },
  headerTitle: {
    fontSize: 17,
    fontWeight: '800',
  },
  headerSub: {
    fontSize: 12,
  },
  menuBtn: {
    padding: 6,
  },
  menuDropdown: {
    position: 'absolute',
    top: 60,
    right: 16,
    width: 220,
    borderRadius: 14,
    borderWidth: 1,
    elevation: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 8,
    zIndex: 100,
  },
  menuItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
  },
  menuText: {
    fontSize: 14,
    fontWeight: '600',
  },
  tabsContainer: {
    flexDirection: 'row',
    borderBottomWidth: 1,
  },
  tab: {
    flex: 1,
    paddingVertical: 14,
    alignItems: 'center',
  },
  tabText: {
    fontSize: 14,
  },
  listContent: {
    paddingHorizontal: 20,
    paddingVertical: 16,
  },
  roomCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
    marginBottom: 14,
  },
  roomHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  roomTitle: {
    fontSize: 16,
    fontWeight: '700',
  },
  completionRingPill: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
  },
  completionText: {
    fontSize: 12,
    fontWeight: '700',
  },
  progressBg: {
    height: 8,
    borderRadius: 4,
    overflow: 'hidden',
    marginBottom: 12,
  },
  progressFill: {
    height: '100%',
    borderRadius: 4,
  },
  roomFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  tapToCaptureText: {
    fontSize: 13,
    fontWeight: '600',
  },
  depositToolsContent: {
    paddingHorizontal: 20,
    paddingVertical: 20,
  },
  quickActionsRow: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 16,
  },
  quickActionBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 44,
    borderRadius: 12,
    gap: 6,
  },
  quickActionText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '700',
  },
  compareHeroCard: {
    borderRadius: 18,
    borderWidth: 1.5,
    padding: 18,
    marginBottom: 20,
  },
  compareHeroHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  heroPill: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
  },
  heroPillText: {
    color: '#FFFFFF',
    fontSize: 10,
    fontWeight: '800',
  },
  compareTitle: {
    fontSize: 18,
    fontWeight: '800',
    marginBottom: 6,
  },
  compareSub: {
    fontSize: 13,
    lineHeight: 18,
  },
  toolCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 18,
    marginBottom: 18,
  },
  toolHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  toolTitle: {
    fontSize: 17,
    fontWeight: '800',
    flex: 1,
  },
  deadlinePill: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 10,
    marginBottom: 16,
  },
  deadlineDaysText: {
    fontSize: 13,
    fontWeight: '700',
  },
  logSectionTitle: {
    fontSize: 14,
    fontWeight: '700',
    marginBottom: 8,
  },
  label: {
    fontSize: 12,
    fontWeight: '600',
    marginBottom: 4,
    marginTop: 6,
  },
  input: {
    height: 42,
    borderRadius: 10,
    borderWidth: 1,
    paddingHorizontal: 12,
    fontSize: 13,
  },
  switchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  saveLogBtn: {
    height: 44,
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    marginVertical: 12,
  },
  saveLogBtnText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '700',
  },
  penaltyBox: {
    padding: 12,
    borderRadius: 12,
    gap: 4,
    marginTop: 4,
  },
  penaltyLabel: {
    fontSize: 12,
    fontWeight: '700',
  },
  penaltyNote: {
    fontSize: 12,
  },
  verifyNote: {
    fontSize: 11,
    fontWeight: '600',
    marginTop: 4,
  },
  proBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
  },
  proBadgeText: {
    fontSize: 11,
    fontWeight: '800',
  },
  toolDesc: {
    fontSize: 13,
    lineHeight: 18,
    marginBottom: 14,
  },
  generateBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 48,
    borderRadius: 14,
  },
  generateBtnText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: '700',
  }
});
