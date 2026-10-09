import React from 'react';
import { StyleSheet, View, Text, Modal, TouchableOpacity, ScrollView } from 'react-native';
import { Shield, Check, Sparkles, X } from 'lucide-react-native';
import { useApp } from '../context/AppContext';

export default function PaywallModal({ visible, onClose }) {
  const { colors, updateProStatus, proStatus } = useApp();

  const handleSelectPlan = async (planType) => {
    await updateProStatus(true, planType);
    onClose();
  };

  const handleRestore = async () => {
    await updateProStatus(true, 'pro_annual');
    onClose();
  };

  if (!visible) return null;

  return (
    <Modal visible={visible} animationType="slide" transparent={false} onRequestClose={onClose}>
      <View style={[styles.container, { backgroundColor: colors.background }]}>
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity onPress={onClose} style={[styles.closeBtn, { backgroundColor: colors.surfaceVariant }]}>
            <X size={20} color={colors.textPrimary} />
          </TouchableOpacity>
          <TouchableOpacity onPress={handleRestore}>
            <Text style={[styles.restoreText, { color: colors.primary }]}>Restore Purchases</Text>
          </TouchableOpacity>
        </View>

        <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
          {/* Hero */}
          <View style={styles.heroSection}>
            <View style={[styles.badge, { backgroundColor: colors.amberLight }]}>
              <Sparkles size={16} color={colors.amber} style={{ marginRight: 6 }} />
              <Text style={[styles.badgeText, { color: colors.amber }]}>MoveProof Pro</Text>
            </View>
            <Text style={[styles.title, { color: colors.textPrimary }]}>
              Your deposit could be worth $1,000+. Protect it for less than a coffee.
            </Text>
            <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
              Unlock pre-existing damage compare tools, formal demand letter generator, clean watermark-free PDFs, and landlord verification links.
            </Text>
          </View>

          {/* Feature Checklist */}
          <View style={[styles.featureCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            {[
              'Unlimited property documentation & rooms',
              'Compare View (Move-In vs Move-Out side-by-side)',
              'Demand Letter Generator auto-filled with legal facts',
              'Landlord Verify Link with SHA-256 web re-verification',
              'Clean, un-watermarked high-res PDF exports',
              'Unlimited photos & 30s video evidence clips'
            ].map((feature, idx) => (
              <View key={idx} style={styles.featureRow}>
                <View style={[styles.checkIcon, { backgroundColor: colors.greenLight }]}>
                  <Check size={16} color={colors.green} strokeWidth={3} />
                </View>
                <Text style={[styles.featureText, { color: colors.textPrimary }]}>{feature}</Text>
              </View>
            ))}
          </View>

          {/* Pricing Options */}
          <View style={styles.plansContainer}>
            {/* Option 1: Pro Annual */}
            <TouchableOpacity
              style={[
                styles.planCard,
                { backgroundColor: colors.surface, borderColor: colors.primary, borderWidth: 2 }
              ]}
              onPress={() => handleSelectPlan('pro_annual')}
              activeOpacity={0.85}
            >
              <View style={[styles.bestValueBadge, { backgroundColor: colors.primary }]}>
                <Text style={styles.bestValueText}>BEST VALUE</Text>
              </View>
              <View style={styles.planHeader}>
                <Text style={[styles.planTitle, { color: colors.textPrimary }]}>Pro Annual</Text>
                <Text style={[styles.planPrice, { color: colors.primary }]}>$29.00 / year</Text>
              </View>
              <Text style={[styles.planSub, { color: colors.textSecondary }]}>
                Unlimited properties, cloud backup, video support, clean PDF exports.
              </Text>
            </TouchableOpacity>

            {/* Option 2: Pro One-Time */}
            <TouchableOpacity
              style={[
                styles.planCard,
                { backgroundColor: colors.surface, borderColor: colors.border, borderWidth: 1 }
              ]}
              onPress={() => handleSelectPlan('pro_property')}
              activeOpacity={0.85}
            >
              <View style={styles.planHeader}>
                <Text style={[styles.planTitle, { color: colors.textPrimary }]}>Pro Single Property</Text>
                <Text style={[styles.planPrice, { color: colors.textPrimary }]}>$4.99 one-time</Text>
              </View>
              <Text style={[styles.planSub, { color: colors.textSecondary }]}>
                Full Pro protection unlocked for 1 rental property forever.
              </Text>
            </TouchableOpacity>
          </View>

          {/* Footer Note */}
          <Text style={[styles.disclaimer, { color: colors.textMuted }]}>
            Subscriptions auto-renew unless cancelled at least 24 hours before period end. Manage in Account Settings.
          </Text>
        </ScrollView>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 48,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    marginBottom: 16,
  },
  closeBtn: {
    width: 36,
    height: 36,
    borderRadius: 18,
    justifyContent: 'center',
    alignItems: 'center',
  },
  restoreText: {
    fontSize: 14,
    fontWeight: '600',
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingBottom: 40,
  },
  heroSection: {
    alignItems: 'center',
    marginVertical: 12,
  },
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
    marginBottom: 16,
  },
  badgeText: {
    fontSize: 13,
    fontWeight: '700',
  },
  title: {
    fontSize: 22,
    fontWeight: '800',
    textAlign: 'center',
    lineHeight: 30,
    marginBottom: 12,
  },
  subtitle: {
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
  },
  featureCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 18,
    marginVertical: 20,
  },
  featureRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  checkIcon: {
    width: 26,
    height: 26,
    borderRadius: 13,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },
  featureText: {
    fontSize: 14,
    fontWeight: '600',
    flex: 1,
  },
  plansContainer: {
    gap: 16,
    marginBottom: 20,
  },
  planCard: {
    borderRadius: 16,
    padding: 18,
    position: 'relative',
  },
  bestValueBadge: {
    position: 'absolute',
    top: -12,
    right: 16,
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 10,
  },
  bestValueText: {
    color: '#FFFFFF',
    fontSize: 10,
    fontWeight: '800',
  },
  planHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  planTitle: {
    fontSize: 17,
    fontWeight: '700',
  },
  planPrice: {
    fontSize: 17,
    fontWeight: '800',
  },
  planSub: {
    fontSize: 13,
    lineHeight: 18,
  },
  disclaimer: {
    fontSize: 11,
    textAlign: 'center',
    lineHeight: 16,
  }
});
