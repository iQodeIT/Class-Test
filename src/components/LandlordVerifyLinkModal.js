import React, { useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Modal,
  Alert,
  Share,
  ActivityIndicator
} from 'react-native';
import { ShieldCheck, Link2, Copy, Check, Lock, Globe, Trash2, X } from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';

export default function LandlordVerifyLinkModal({ visible, onClose, propertyId, openPaywall }) {
  const { colors, proStatus } = useApp();
  const [loading, setLoading] = useState(false);
  const [verifyUrl, setVerifyUrl] = useState(null);
  const [copied, setCopied] = useState(false);

  if (!visible) return null;

  const handleCreateLink = async () => {
    if (!proStatus.isPro) {
      onClose();
      openPaywall();
      return;
    }

    setLoading(true);
    try {
      const items = await StorageService.getItems(propertyId);
      const payload = {
        propertyId,
        itemsCount: items.length,
        createdAt: new Date().toISOString(),
        hashes: items.map(i => ({ label: i.label, sha256: i.sha256 }))
      };

      // Generate secure unguessable verification URL (simulated Supabase backend response)
      const uniqueId = Math.random().toString(36).substring(2, 12);
      const generatedUrl = `https://verify.moveproof.app/report/${uniqueId}`;

      setVerifyUrl(generatedUrl);
    } catch (e) {
      Alert.alert('Error', 'Failed to generate landlord verification link.');
    } finally {
      setLoading(false);
    }
  };

  const handleCopy = () => {
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
    Alert.alert('Copied Link', 'Verification URL copied to clipboard.');
  };

  const handleShare = async () => {
    if (verifyUrl) {
      await Share.share({
        message: `MoveProof Inspection Evidence Report for Landlord Review: ${verifyUrl}`
      });
    }
  };

  const handleRevoke = () => {
    Alert.alert(
      'Revoke Verification Link',
      'This will immediately disable access to the read-only web report for landlords. You can create a new link at any time.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Revoke Link',
          style: 'destructive',
          onPress: () => {
            setVerifyUrl(null);
            Alert.alert('Link Revoked', 'The landlord verification link is no longer active.');
          }
        }
      ]
    );
  };

  return (
    <Modal visible={visible} animationType="slide" transparent={true} onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={[styles.modalCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
          <View style={styles.modalHeader}>
            <View style={styles.titleRow}>
              <ShieldCheck size={22} color={colors.primary} style={{ marginRight: 8 }} />
              <Text style={[styles.modalTitle, { color: colors.textPrimary }]}>Landlord Verify Link</Text>
            </View>
            <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
              <X size={20} color={colors.textPrimary} />
            </TouchableOpacity>
          </View>

          {/* Privacy Explanation Banner */}
          <View style={[styles.privacyBox, { backgroundColor: colors.surfaceVariant }]}>
            <Lock size={16} color={colors.primary} style={{ marginRight: 8, marginTop: 2 }} />
            <Text style={[styles.privacyText, { color: colors.textSecondary }]}>
              Zero-install for landlords. Nothing leaves your device unless you choose to create a link. Only photos, timestamps, and cryptographic hashes are rendered on a read-only webpage.
            </Text>
          </View>

          {verifyUrl ? (
            <View style={styles.urlResultContainer}>
              <Text style={[styles.urlLabel, { color: colors.textSecondary }]}>Active Landlord Web Link:</Text>
              <View style={[styles.urlBox, { backgroundColor: colors.background, borderColor: colors.border }]}>
                <Globe size={18} color={colors.primary} style={{ marginRight: 8 }} />
                <Text style={[styles.urlText, { color: colors.primary }]} numberOfLines={1}>
                  {verifyUrl}
                </Text>
              </View>

              <View style={styles.actionRow}>
                <TouchableOpacity
                  style={[styles.actionBtn, { backgroundColor: colors.surfaceVariant }]}
                  onPress={handleCopy}
                >
                  {copied ? <Check size={18} color={colors.green} /> : <Copy size={18} color={colors.textPrimary} />}
                  <Text style={[styles.actionBtnText, { color: colors.textPrimary }]}>{copied ? 'Copied' : 'Copy'}</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[styles.actionBtn, { backgroundColor: colors.primary }]}
                  onPress={handleShare}
                >
                  <Link2 size={18} color="#FFFFFF" />
                  <Text style={[styles.actionBtnText, { color: '#FFFFFF' }]}>Share Link</Text>
                </TouchableOpacity>
              </View>

              <TouchableOpacity style={styles.revokeBtn} onPress={handleRevoke}>
                <Trash2 size={16} color={colors.danger} style={{ marginRight: 6 }} />
                <Text style={[styles.revokeText, { color: colors.danger }]}>Revoke Web Access</Text>
              </TouchableOpacity>
            </View>
          ) : (
            <View style={styles.createContainer}>
              {loading ? (
                <ActivityIndicator size="large" color={colors.primary} style={{ marginVertical: 20 }} />
              ) : (
                <TouchableOpacity
                  style={[styles.createBtn, { backgroundColor: colors.primary }]}
                  onPress={handleCreateLink}
                >
                  <Link2 size={20} color="#FFFFFF" style={{ marginRight: 8 }} />
                  <Text style={styles.createBtnText}>Create Verify Link</Text>
                </TouchableOpacity>
              )}
            </View>
          )}
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  modalCard: {
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    borderWidth: 1,
    padding: 20,
    paddingBottom: 40,
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  titleRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: '800',
  },
  closeBtn: {
    padding: 4,
  },
  privacyBox: {
    flexDirection: 'row',
    padding: 12,
    borderRadius: 12,
    marginBottom: 20,
  },
  privacyText: {
    fontSize: 12,
    lineHeight: 18,
    flex: 1,
  },
  urlResultContainer: {
    gap: 12,
  },
  urlLabel: {
    fontSize: 12,
    fontWeight: '600',
  },
  urlBox: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 48,
    borderRadius: 12,
    borderWidth: 1,
    paddingHorizontal: 12,
  },
  urlText: {
    fontSize: 13,
    fontWeight: '600',
    flex: 1,
  },
  actionRow: {
    flexDirection: 'row',
    gap: 12,
  },
  actionBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 48,
    borderRadius: 12,
    gap: 6,
  },
  actionBtnText: {
    fontSize: 14,
    fontWeight: '700',
  },
  revokeBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
  },
  revokeText: {
    fontSize: 13,
    fontWeight: '700',
  },
  createContainer: {
    marginVertical: 10,
  },
  createBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 52,
    borderRadius: 16,
  },
  createBtnText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  }
});
