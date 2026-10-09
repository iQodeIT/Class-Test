import React from 'react';
import {
  StyleSheet,
  View,
  Text,
  SafeAreaView,
  ScrollView,
  TouchableOpacity,
  Alert,
  Switch
} from 'react-native';
import {
  User,
  Sparkles,
  Download,
  Trash2,
  Moon,
  Sun,
  Shield,
  HelpCircle,
  FileText,
  CheckCircle2
} from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';

export default function SettingsScreen({ openPaywall }) {
  const { colors, themeMode, setThemeMode, proStatus, updateProStatus } = useApp();

  const handleExportAllData = async () => {
    try {
      const data = await StorageService.exportAllData();
      Alert.alert(
        'Data Export Ready',
        `Successfully exported ${data.properties.length} property records and ${data.items.length} inspection evidence items.`
      );
    } catch (e) {
      Alert.alert('Export Error', 'Failed to export data.');
    }
  };

  const handleDeleteAllData = () => {
    Alert.alert(
      'Delete All Data',
      'Are you sure you want to permanently erase all properties, inspection photos, hashes, and local logs? This action CANNOT be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Permanently Erase',
          style: 'destructive',
          onPress: async () => {
            await StorageService.clearAllData();
            Alert.alert('Data Cleared', 'All local MoveProof data has been deleted.');
          }
        }
      ]
    );
  };

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      <View style={styles.header}>
        <Text style={[styles.headerTitle, { color: colors.textPrimary }]}>Settings</Text>
        <Text style={[styles.headerSub, { color: colors.textSecondary }]}>Account, storage, theme & support</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Pro Plan Status Card */}
        <TouchableOpacity
          style={[styles.proCard, { backgroundColor: colors.surface, borderColor: proStatus.isPro ? colors.green : colors.amber }]}
          onPress={openPaywall}
          activeOpacity={0.85}
        >
          <View style={styles.proCardHeader}>
            <View style={[styles.proBadgePill, { backgroundColor: proStatus.isPro ? colors.greenLight : colors.amberLight }]}>
              <Sparkles size={16} color={proStatus.isPro ? colors.green : colors.amber} style={{ marginRight: 6 }} />
              <Text style={[styles.proBadgeText, { color: proStatus.isPro ? colors.green : colors.amber }]}>
                {proStatus.isPro ? 'MoveProof Pro Active' : 'Free Tier'}
              </Text>
            </View>
            <Text style={[styles.proActionText, { color: colors.primary }]}>
              {proStatus.isPro ? 'Manage' : 'Upgrade'}
            </Text>
          </View>
          <Text style={[styles.proCardSub, { color: colors.textSecondary }]}>
            {proStatus.isPro
              ? 'You have full access to unlimited properties, compare view, demand letter generator, and landlord verify links.'
              : 'Upgrade to unlock pre-existing damage compare tools, formal demand letters, and landlord verify links.'}
          </Text>
        </TouchableOpacity>

        {/* Theme Settings */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Appearance & Theme</Text>
          <View style={[styles.card, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <View style={styles.row}>
              <View style={styles.rowLeft}>
                <Moon size={20} color={colors.primary} style={{ marginRight: 12 }} />
                <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>Dark Mode</Text>
              </View>
              <Switch
                value={themeMode === 'dark'}
                onValueChange={(val) => setThemeMode(val ? 'dark' : 'light')}
                trackColor={{ false: colors.border, true: colors.primary }}
              />
            </View>
          </View>
        </View>

        {/* Storage & Backup */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Data & Backup</Text>
          <View style={[styles.card, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <TouchableOpacity style={styles.rowItem} onPress={handleExportAllData}>
              <Download size={20} color={colors.primary} style={{ marginRight: 12 }} />
              <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>Export All Evidence Data (JSON)</Text>
            </TouchableOpacity>

            <View style={[styles.divider, { backgroundColor: colors.border }]} />

            <TouchableOpacity style={styles.rowItem} onPress={handleDeleteAllData}>
              <Trash2 size={20} color={colors.danger} style={{ marginRight: 12 }} />
              <Text style={[styles.rowLabel, { color: colors.danger }]}>Delete All Local Data</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Support & Legal */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Support & Legal</Text>
          <View style={[styles.card, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <TouchableOpacity style={styles.rowItem} onPress={() => Alert.alert('Contact Support', 'Email support@moveproof.app')}>
              <HelpCircle size={20} color={colors.primary} style={{ marginRight: 12 }} />
              <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>Contact Support</Text>
            </TouchableOpacity>

            <View style={[styles.divider, { backgroundColor: colors.border }]} />

            <TouchableOpacity style={styles.rowItem} onPress={() => Alert.alert('Privacy Policy', 'MoveProof stores all evidence local-first on device.')}>
              <Shield size={20} color={colors.primary} style={{ marginRight: 12 }} />
              <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>Privacy Policy</Text>
            </TouchableOpacity>

            <View style={[styles.divider, { backgroundColor: colors.border }]} />

            <TouchableOpacity style={styles.rowItem} onPress={() => Alert.alert('Terms of Service', 'MoveProof standard terms apply.')}>
              <FileText size={20} color={colors.primary} style={{ marginRight: 12 }} />
              <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>Terms of Service</Text>
            </TouchableOpacity>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    paddingHorizontal: 20,
    paddingTop: 16,
    paddingBottom: 12,
  },
  headerTitle: {
    fontSize: 26,
    fontWeight: '800',
  },
  headerSub: {
    fontSize: 13,
    marginTop: 2,
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingBottom: 40,
  },
  proCard: {
    borderRadius: 18,
    borderWidth: 1.5,
    padding: 16,
    marginBottom: 20,
  },
  proCardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  proBadgePill: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
  },
  proBadgeText: {
    fontSize: 12,
    fontWeight: '800',
  },
  proActionText: {
    fontSize: 14,
    fontWeight: '700',
  },
  proCardSub: {
    fontSize: 13,
    lineHeight: 18,
  },
  section: {
    marginBottom: 20,
  },
  sectionTitle: {
    fontSize: 15,
    fontWeight: '800',
    marginBottom: 8,
  },
  card: {
    borderRadius: 16,
    borderWidth: 1,
    overflow: 'hidden',
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 16,
  },
  rowLeft: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  rowItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
  },
  rowLabel: {
    fontSize: 14,
    fontWeight: '600',
  },
  divider: {
    height: 1,
  }
});
