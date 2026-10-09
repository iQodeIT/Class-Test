import React, { useState, useEffect } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  FlatList,
  RefreshControl,
  Image,
  Dimensions
} from 'react-native';
import { Plus, Shield, WifiOff, ChevronRight, Home, Calendar, AlertCircle } from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';

const { width } = Dimensions.get('window');

export default function PropertiesHomeScreen({ navigateTo, openPaywall }) {
  const { colors, properties, refreshProperties, proStatus } = useApp();
  const [activeFilter, setActiveFilter] = useState('All'); // 'All', 'Move-In', 'Move-Out', 'Complete'
  const [propertyStats, setPropertyStats] = useState({});
  const [refreshing, setRefreshing] = useState(false);

  // Calculate progress stats & countdowns for each property
  const loadStats = async () => {
    const stats = {};
    for (const prop of properties) {
      const rooms = await StorageService.getRooms(prop.id);
      const items = await StorageService.getItems(prop.id);

      const moveInItems = items.filter(i => i.phase === 'move_in');
      const moveOutItems = items.filter(i => i.phase === 'move_out');

      const totalPossibleItems = rooms.reduce((acc, r) => acc + (r.checklist?.length || 6), 0) || 1;

      const moveInPct = Math.min(100, Math.round((moveInItems.length / totalPossibleItems) * 100));
      const moveOutPct = Math.min(100, Math.round((moveOutItems.length / totalPossibleItems) * 100));

      // Calculate deposit countdown
      let countdownDays = null;
      if (prop.moveOutDate) {
        const moveOut = new Date(prop.moveOutDate);
        const today = new Date();
        const diffTime = moveOut.getTime() - today.getTime();
        countdownDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      }

      stats[prop.id] = {
        moveInPct,
        moveOutPct,
        totalRooms: rooms.length,
        itemsCaptured: items.length,
        countdownDays,
        lastPhotoUri: items.find(i => i.mediaUri)?.mediaUri || null,
        status: prop.status || (moveOutPct > 80 ? 'Complete' : moveInPct > 80 ? 'Move-Out' : 'Move-In')
      };
    }
    setPropertyStats(stats);
  };

  useEffect(() => {
    loadStats();
  }, [properties]);

  const onRefresh = async () => {
    setRefreshing(true);
    await refreshProperties();
    await loadStats();
    setRefreshing(false);
  };

  const handleAddPropertyPress = () => {
    if (!proStatus.isPro && properties.length >= 1) {
      openPaywall();
    } else {
      navigateTo('ADD_PROPERTY');
    }
  };

  // Filter properties
  const filteredProperties = properties.filter(prop => {
    if (activeFilter === 'All') return true;
    const stat = propertyStats[prop.id];
    if (!stat) return true;
    if (activeFilter === 'Move-In') return stat.status === 'Move-In';
    if (activeFilter === 'Move-Out') return stat.status === 'Move-Out';
    if (activeFilter === 'Complete') return stat.status === 'Complete';
    return true;
  });

  const renderPropertyCard = ({ item }) => {
    const stat = propertyStats[item.id] || { moveInPct: 0, moveOutPct: 0, status: 'Move-In' };
    const statusColor = stat.status === 'Complete' ? colors.green : stat.status === 'Move-Out' ? colors.amber : colors.primary;

    return (
      <TouchableOpacity
        style={[
          styles.card,
          { backgroundColor: colors.surface, borderColor: colors.border }
        ]}
        onPress={() => navigateTo('PROPERTY_DETAIL', { propertyId: item.id })}
        activeOpacity={0.85}
      >
        <View style={styles.cardHeader}>
          {/* Thumbnail */}
          <View style={[styles.thumbnail, { backgroundColor: colors.surfaceVariant }]}>
            {stat.lastPhotoUri ? (
              <Image source={{ uri: stat.lastPhotoUri }} style={styles.thumbnailImg} />
            ) : (
              <Home size={28} color={colors.textMuted} />
            )}
          </View>

          {/* Info */}
          <View style={styles.cardInfo}>
            <View style={styles.titleRow}>
              <Text style={[styles.nickname, { color: colors.textPrimary }]} numberOfLines={1}>
                {item.nickname || item.address}
              </Text>
              <View style={[styles.statusBadge, { backgroundColor: `${statusColor}20` }]}>
                <Text style={[styles.statusBadgeText, { color: statusColor }]}>{stat.status}</Text>
              </View>
            </View>

            <Text style={[styles.address, { color: colors.textSecondary }]} numberOfLines={1}>
              {item.address}
            </Text>

            {item.depositAmount ? (
              <Text style={[styles.depositText, { color: colors.primary }]}>
                Deposit: ${Number(item.depositAmount).toLocaleString()}
              </Text>
            ) : null}
          </View>
        </View>

        {/* Deposit Countdown Chip if moveOutDate set */}
        {stat.countdownDays !== null && (
          <View style={[
            styles.countdownChip,
            { backgroundColor: stat.countdownDays <= 7 ? colors.amberLight : colors.primaryLight }
          ]}>
            <Calendar size={14} color={stat.countdownDays <= 7 ? colors.amber : colors.primary} />
            <Text style={[
              styles.countdownText,
              { color: stat.countdownDays <= 7 ? colors.amber : colors.primary }
            ]}>
              {stat.countdownDays < 0
                ? `Deposit return overdue by ${Math.abs(stat.countdownDays)} days`
                : `Deposit due back in ${stat.countdownDays} days`}
            </Text>
          </View>
        )}

        {/* Progress Bars */}
        <View style={styles.progressContainer}>
          {/* Move-In Progress */}
          <View style={styles.progressRow}>
            <View style={styles.progressLabelRow}>
              <Text style={[styles.progressLabel, { color: colors.textSecondary }]}>Move-In Capture</Text>
              <Text style={[styles.progressPct, { color: colors.textPrimary }]}>{stat.moveInPct}%</Text>
            </View>
            <View style={[styles.progressBarBg, { backgroundColor: colors.surfaceVariant }]}>
              <View
                style={[
                  styles.progressBarFill,
                  { width: `${stat.moveInPct}%`, backgroundColor: colors.primary }
                ]}
              />
            </View>
          </View>

          {/* Move-Out Progress */}
          <View style={styles.progressRow}>
            <View style={styles.progressLabelRow}>
              <Text style={[styles.progressLabel, { color: colors.textSecondary }]}>Move-Out Capture</Text>
              <Text style={[styles.progressPct, { color: colors.textPrimary }]}>{stat.moveOutPct}%</Text>
            </View>
            <View style={[styles.progressBarBg, { backgroundColor: colors.surfaceVariant }]}>
              <View
                style={[
                  styles.progressBarFill,
                  { width: `${stat.moveOutPct}%`, backgroundColor: colors.amber }
                ]}
              />
            </View>
          </View>
        </View>

        {/* Card Actions */}
        <TouchableOpacity
          style={[styles.continueBtn, { backgroundColor: colors.primaryLight }]}
          onPress={() => navigateTo('ROOM_CAPTURE', { propertyId: item.id, roomId: item.lastUnfinishedRoomId })}
          activeOpacity={0.8}
        >
          <Text style={[styles.continueBtnText, { color: colors.primary }]}>Continue capturing</Text>
          <ChevronRight size={18} color={colors.primary} />
        </TouchableOpacity>
      </TouchableOpacity>
    );
  };

  return (
    <View style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Header */}
      <View style={styles.header}>
        <View>
          <Text style={[styles.headerTitle, { color: colors.textPrimary }]}>Properties</Text>
          <Text style={[styles.headerSubtitle, { color: colors.textSecondary }]}>
            {properties.length} {properties.length === 1 ? 'rental' : 'rentals'} documented
          </Text>
        </View>

        {/* Works Offline Badge */}
        <View style={[styles.offlineBadge, { backgroundColor: colors.surfaceVariant, borderColor: colors.border }]}>
          <WifiOff size={14} color={colors.textSecondary} style={{ marginRight: 6 }} />
          <Text style={[styles.offlineText, { color: colors.textSecondary }]}>Works offline</Text>
        </View>
      </View>

      {/* Filter Chips */}
      <View style={styles.filtersRow}>
        {['All', 'Move-In', 'Move-Out', 'Complete'].map((filter) => {
          const isSelected = activeFilter === filter;
          return (
            <TouchableOpacity
              key={filter}
              style={[
                styles.filterChip,
                {
                  backgroundColor: isSelected ? colors.primary : colors.surface,
                  borderColor: isSelected ? colors.primary : colors.border
                }
              ]}
              onPress={() => setActiveFilter(filter)}
            >
              <Text
                style={[
                  styles.filterChipText,
                  { color: isSelected ? '#FFFFFF' : colors.textSecondary }
                ]}
              >
                {filter}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>

      {/* Properties List or Zero-Property Empty State */}
      {properties.length === 0 ? (
        <View style={styles.emptyState}>
          <View style={[styles.emptyIconCircle, { backgroundColor: colors.primaryLight }]}>
            <Shield size={48} color={colors.primary} />
          </View>
          <Text style={[styles.emptyTitle, { color: colors.textPrimary }]}>No Rental Properties Yet</Text>
          <Text style={[styles.emptySub, { color: colors.textSecondary }]}>
            Add your rental to document move-in condition, protect against pre-existing damage charges, and calculate deposit return deadlines.
          </Text>
          <TouchableOpacity
            style={[styles.addFirstBtn, { backgroundColor: colors.primary }]}
            onPress={handleAddPropertyPress}
          >
            <Plus size={20} color="#FFFFFF" style={{ marginRight: 8 }} />
            <Text style={styles.addFirstBtnText}>Add Your First Property</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={filteredProperties}
          keyExtractor={(item) => item.id}
          renderItem={renderPropertyCard}
          contentContainerStyle={styles.listContent}
          showsVerticalScrollIndicator={false}
          refreshControl={
            <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={colors.primary} />
          }
        />
      )}

      {/* Floating Add Property Button (Sits safely above bottom tab bar) */}
      {properties.length > 0 && (
        <TouchableOpacity
          style={[styles.fab, { backgroundColor: colors.primary }]}
          onPress={handleAddPropertyPress}
          activeOpacity={0.85}
        >
          <Plus size={22} color="#FFFFFF" style={{ marginRight: 6 }} />
          <Text style={styles.fabText}>Add Property</Text>
        </TouchableOpacity>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    position: 'relative',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: 16,
    paddingBottom: 12,
  },
  headerTitle: {
    fontSize: 26,
    fontWeight: '800',
    letterSpacing: -0.5,
  },
  headerSubtitle: {
    fontSize: 13,
    marginTop: 2,
  },
  offlineBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 20,
    borderWidth: 1,
  },
  offlineText: {
    fontSize: 12,
    fontWeight: '600',
  },
  filtersRow: {
    flexDirection: 'row',
    paddingHorizontal: 20,
    marginBottom: 12,
    gap: 8,
  },
  filterChip: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    borderWidth: 1,
  },
  filterChipText: {
    fontSize: 13,
    fontWeight: '600',
  },
  listContent: {
    paddingHorizontal: 20,
    paddingBottom: 100, // Extra space for FAB
  },
  card: {
    borderRadius: 18,
    borderWidth: 1,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 2,
  },
  cardHeader: {
    flexDirection: 'row',
    marginBottom: 12,
  },
  thumbnail: {
    width: 64,
    height: 64,
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden',
    marginRight: 14,
  },
  thumbnailImg: {
    width: '100%',
    height: '100%',
  },
  cardInfo: {
    flex: 1,
    justifyContent: 'center',
  },
  titleRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 4,
  },
  nickname: {
    fontSize: 17,
    fontWeight: '700',
    flex: 1,
    marginRight: 8,
  },
  statusBadge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  statusBadgeText: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
  },
  address: {
    fontSize: 13,
    marginBottom: 4,
  },
  depositText: {
    fontSize: 13,
    fontWeight: '700',
  },
  countdownChip: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 10,
    marginBottom: 12,
  },
  countdownText: {
    fontSize: 12,
    fontWeight: '700',
    marginLeft: 6,
  },
  progressContainer: {
    gap: 8,
    marginBottom: 14,
  },
  progressRow: {
    gap: 4,
  },
  progressLabelRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  progressLabel: {
    fontSize: 12,
    fontWeight: '600',
  },
  progressPct: {
    fontSize: 12,
    fontWeight: '700',
  },
  progressBarBg: {
    height: 8,
    borderRadius: 4,
    overflow: 'hidden',
  },
  progressBarFill: {
    height: '100%',
    borderRadius: 4,
  },
  continueBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 44,
    borderRadius: 12,
  },
  continueBtnText: {
    fontSize: 14,
    fontWeight: '700',
    marginRight: 4,
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 32,
    marginTop: 40,
  },
  emptyIconCircle: {
    width: 96,
    height: 96,
    borderRadius: 48,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 20,
  },
  emptyTitle: {
    fontSize: 20,
    fontWeight: '800',
    marginBottom: 8,
  },
  emptySub: {
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
    marginBottom: 24,
  },
  addFirstBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 52,
    paddingHorizontal: 24,
    borderRadius: 16,
  },
  addFirstBtnText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  },
  fab: {
    position: 'absolute',
    bottom: 16,
    right: 20,
    flexDirection: 'row',
    alignItems: 'center',
    height: 52,
    paddingHorizontal: 20,
    borderRadius: 26,
    elevation: 6,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.25,
    shadowRadius: 8,
  },
  fabText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: '700',
  }
});
