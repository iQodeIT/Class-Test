import React, { useState, useEffect } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  SafeAreaView,
  ScrollView,
  TextInput,
  Alert,
  Share
} from 'react-native';
import * as Print from 'expo-print';
import {
  ArrowLeft,
  FileText,
  Copy,
  Mail,
  Share2,
  Scale,
  Sparkles,
  Check
} from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';
import { getStateRule } from '../config/depositRules';

export default function DemandLetterScreen({ params, goBack, openPaywall }) {
  const { propertyId } = params;
  const { colors, proStatus } = useApp();

  const [property, setProperty] = useState(null);
  const [landlordName, setLandlordName] = useState('');
  const [landlordAddress, setLandlordAddress] = useState('');
  const [depositAmount, setDepositAmount] = useState('');
  const [amountReturned, setAmountReturned] = useState('0');
  const [disputedDeductions, setDisputedDeductions] = useState('');
  const [letterText, setLetterText] = useState('');
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    (async () => {
      const props = await StorageService.getProperties();
      const prop = props.find(p => p.id === propertyId);
      setProperty(prop);

      if (prop) {
        setLandlordName(prop.landlordName || 'Landlord / Property Manager');
        setDepositAmount(prop.depositAmount ? prop.depositAmount.toString() : '2000');
        setLandlordAddress(prop.address);

        // Fetch pre-existing items for auto-filling facts paragraph
        const allItems = await StorageService.getItems(propertyId);
        const preExistingItems = allItems.filter(i => i.isPreExisting || i.condition === 'Pre-existing damage');

        const stateRule = getStateRule(prop.region);
        const today = new Date().toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });

        let preExistingParagraph = '';
        if (preExistingItems.length > 0) {
          const itemNames = preExistingItems.map(i => i.label).join(', ');
          preExistingParagraph = `\nFurthermore, MoveProof cryptographic time-stamped inspection records confirm that pre-existing condition baseline photos for (${itemNames}) were documented on ${new Date(prop.createdAt || Date.now()).toLocaleDateString()}. These pre-existing defects cannot legally be deducted from my security deposit.`;
        }

        const generated = `DEMAND FOR RETURN OF SECURITY DEPOSIT

Date: ${today}

TO: ${prop.landlordName || 'Landlord'}
PROPERTY: ${prop.address}

Dear ${prop.landlordName || 'Landlord'},

This letter serves as formal written demand for the full return of my security deposit in the amount of $${prop.depositAmount || '2,000.00'} for the tenancy at ${prop.address}.

Under ${stateRule.stateName} statutory landlord-tenant laws, security deposits must be returned within ${stateRule.daysAllowed} days of surrender of the premises, along with an itemized statement of any lawful deductions.

To date, you have returned $${amountReturned || '0.00'}. The remaining withheld balance of $${(parseFloat(prop.depositAmount || 2000) - parseFloat(amountReturned || 0)).toFixed(2)} is disputed.${preExistingParagraph}

Please remit payment for the full remaining balance within seven (7) business days to my forwarding address. If this matter is not resolved promptly, I reserve all rights to pursue legal remedies in Small Claims Court, where statutory penalties of ${stateRule.penaltyNote} may apply.

Sincerely,

[Renter Signature & Forwarding Address]
`;
        setLetterText(generated);
      }
    })();
  }, [propertyId]);

  const handleCopyText = () => {
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
    Alert.alert('Copied to Clipboard', 'Demand letter text copied to clipboard.');
  };

  const handleExportPDF = async () => {
    try {
      const htmlContent = `
        <!DOCTYPE html>
        <html>
          <head>
            <meta charset="utf-8">
            <style>
              body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; padding: 40px; color: #1e293b; line-height: 1.6; }
              .header { border-bottom: 2px solid #005F73; padding-bottom: 12px; margin-bottom: 24px; }
              .title { font-size: 20px; font-weight: bold; color: #005F73; text-transform: uppercase; }
              .content { white-space: pre-wrap; font-size: 14px; }
              .disclaimer { margin-top: 40px; padding: 12px; border-top: 1px solid #cbd5e1; font-size: 10px; color: #64748b; }
            </style>
          </head>
          <body>
            <div class="header">
              <div class="title">Formal Demand Letter</div>
            </div>
            <div class="content">${letterText}</div>
            <div class="disclaimer">
              DISCLAIMER: MoveProof provides general information tools and templates, not legal advice. If you require legal representation, consult a licensed attorney or legal aid organization in your jurisdiction.
            </div>
          </body>
        </html>
      `;

      const { uri } = await Print.printToFileAsync({ html: htmlContent });
      await Share.share({
        title: 'Demand Letter PDF',
        url: uri
      });
    } catch (e) {
      console.error('PDF export error:', e);
    }
  };

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Header */}
      <View style={[styles.header, { borderBottomColor: colors.border }]}>
        <TouchableOpacity onPress={goBack} style={styles.backBtn}>
          <ArrowLeft size={24} color={colors.textPrimary} />
        </TouchableOpacity>
        <Text style={[styles.headerTitle, { color: colors.textPrimary }]}>Demand Letter Generator</Text>
        <View style={[styles.proPill, { backgroundColor: colors.amberLight }]}>
          <Sparkles size={12} color={colors.amber} style={{ marginRight: 4 }} />
          <Text style={[styles.proPillText, { color: colors.amber }]}>PRO</Text>
        </View>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Disclaimers Box */}
        <View style={[styles.disclaimerBox, { backgroundColor: colors.surfaceVariant, borderColor: colors.border }]}>
          <Scale size={18} color={colors.primary} style={{ marginRight: 8 }} />
          <Text style={[styles.disclaimerText, { color: colors.textSecondary }]}>
            General legal information template. Not legal advice. Confirm local rules before sending.
          </Text>
        </View>

        {/* Form Inputs */}
        <View style={[styles.formCard, { backgroundColor: colors.surface, borderColor: colors.border }]}>
          <Text style={[styles.formTitle, { color: colors.textPrimary }]}>Letter Facts</Text>

          <Text style={[styles.label, { color: colors.textSecondary }]}>Landlord Name</Text>
          <TextInput
            style={[styles.input, { backgroundColor: colors.background, borderColor: colors.border, color: colors.textPrimary }]}
            value={landlordName}
            onChangeText={setLandlordName}
          />

          <Text style={[styles.label, { color: colors.textSecondary }]}>Amount Returned So Far ($)</Text>
          <TextInput
            style={[styles.input, { backgroundColor: colors.background, borderColor: colors.border, color: colors.textPrimary }]}
            value={amountReturned}
            keyboardType="numeric"
            onChangeText={setAmountReturned}
          />
        </View>

        {/* Letter Output Display */}
        <Text style={[styles.outputHeader, { color: colors.textPrimary }]}>Generated Letter</Text>
        <View style={[styles.letterBox, { backgroundColor: colors.surface, borderColor: colors.border }]}>
          <TextInput
            style={[styles.letterText, { color: colors.textPrimary }]}
            multiline
            value={letterText}
            onChangeText={setLetterText}
          />
        </View>

        {/* Action Buttons */}
        <View style={styles.actionsRow}>
          <TouchableOpacity
            style={[styles.actionBtn, { backgroundColor: colors.surface, borderColor: colors.border, borderWidth: 1 }]}
            onPress={handleCopyText}
          >
            {copied ? <Check size={18} color={colors.green} /> : <Copy size={18} color={colors.textPrimary} />}
            <Text style={[styles.actionBtnText, { color: colors.textPrimary }]}>{copied ? 'Copied' : 'Copy Text'}</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.actionBtn, { backgroundColor: colors.primary }]}
            onPress={handleExportPDF}
          >
            <FileText size={18} color="#FFFFFF" />
            <Text style={[styles.actionBtnText, { color: '#FFFFFF' }]}>Export PDF</Text>
          </TouchableOpacity>
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
    height: 56,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    justifyContent: 'space-between',
    borderBottomWidth: 1,
  },
  backBtn: {
    padding: 6,
  },
  headerTitle: {
    fontSize: 17,
    fontWeight: '800',
  },
  proPill: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  proPillText: {
    fontSize: 11,
    fontWeight: '800',
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingVertical: 16,
  },
  disclaimerBox: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderRadius: 12,
    borderWidth: 1,
    marginBottom: 16,
  },
  disclaimerText: {
    fontSize: 12,
    flex: 1,
  },
  formCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
    marginBottom: 16,
  },
  formTitle: {
    fontSize: 15,
    fontWeight: '700',
    marginBottom: 10,
  },
  label: {
    fontSize: 12,
    fontWeight: '600',
    marginBottom: 4,
    marginTop: 6,
  },
  input: {
    height: 44,
    borderRadius: 10,
    borderWidth: 1,
    paddingHorizontal: 12,
    fontSize: 14,
  },
  outputHeader: {
    fontSize: 15,
    fontWeight: '700',
    marginBottom: 8,
  },
  letterBox: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
    marginBottom: 16,
  },
  letterText: {
    fontSize: 13,
    lineHeight: 20,
  },
  actionsRow: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 40,
  },
  actionBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 50,
    borderRadius: 14,
    gap: 6,
  },
  actionBtnText: {
    fontSize: 14,
    fontWeight: '700',
  }
});
