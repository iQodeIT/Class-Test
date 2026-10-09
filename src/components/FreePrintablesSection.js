import React, { useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  SafeAreaView,
  ScrollView,
  Share,
  Alert
} from 'react-native';
import * as Print from 'expo-print';
import { FileText, Download, Share2, Check, Sparkles } from 'lucide-react-native';
import { useApp } from '../context/AppContext';

export default function FreePrintablesSection({ openPaywall }) {
  const { colors } = useApp();

  const handleGeneratePrintable = async (type) => {
    try {
      let title = "Move-In Checklist";
      let bodyHtml = "";

      if (type === 'move_in') {
        title = "Move-In Rental Condition Checklist";
        bodyHtml = `
          <h2>Move-In Rental Condition Checklist</h2>
          <p>Document room conditions thoroughly prior to signing or moving furniture.</p>
          <table>
            <tr><th>Room / Area</th><th>Good</th><th>Minor Wear</th><th>Damaged</th><th>Notes & Details</th></tr>
            <tr><td>Living Room - Walls & Baseboards</td><td>[ ]</td><td>[ ]</td><td>[ ]</td><td>____________________</td></tr>
            <tr><td>Kitchen - Stove & Oven</td><td>[ ]</td><td>[ ]</td><td>[ ]</td><td>____________________</td></tr>
            <tr><td>Kitchen - Refrigerator</td><td>[ ]</td><td>[ ]</td><td>[ ]</td><td>____________________</td></tr>
            <tr><td>Bathroom - Tub / Shower</td><td>[ ]</td><td>[ ]</td><td>[ ]</td><td>____________________</td></tr>
            <tr><td>Bedroom - Windows & Blinds</td><td>[ ]</td><td>[ ]</td><td>[ ]</td><td>____________________</td></tr>
          </table>
        `;
      } else if (type === 'cleaning') {
        title = "Move-Out Deep Cleaning Checklist";
        bodyHtml = `
          <h2>Move-Out Deep Cleaning Checklist</h2>
          <p>Ensure maximum deposit return by completing these critical cleaning steps.</p>
          <ul>
            <li>[ ] Wipe inside & outside of kitchen cabinets and oven</li>
            <li>[ ] Defrost freezer and wipe down refrigerator interior</li>
            <li>[ ] Scrub bathroom tub, tiles, and toilet base</li>
            <li>[ ] Steam clean carpets and wipe window sills</li>
            <li>[ ] Patch nail holes in walls and wipe baseboards</li>
          </ul>
        `;
      } else {
        title = "Renter Security Deposit Binder Cover";
        bodyHtml = `
          <div style="text-align: center; margin-top: 100px;">
            <h1 style="font-size: 32px; color: #005F73;">RENTER LEASE & EVIDENCE BINDER</h1>
            <p style="font-size: 18px; margin-top: 20px;">Property Address: _____________________________________</p>
            <p style="font-size: 16px;">Tenant Name: __________________________________________</p>
            <p style="font-size: 16px;">Lease Period: ___________________ to ___________________</p>
          </div>
        `;
      }

      const html = `
        <!DOCTYPE html>
        <html>
          <head>
            <meta charset="utf-8">
            <style>
              body { font-family: Arial, sans-serif; padding: 30px; color: #1e293b; }
              h2 { color: #005F73; border-bottom: 2px solid #005F73; padding-bottom: 8px; }
              table { width: 100%; border-collapse: collapse; margin-top: 20px; }
              th, td { border: 1px solid #cbd5e1; padding: 10px; text-align: left; }
              th { background-color: #f1f5f9; }
              .footer { margin-top: 60px; font-size: 11px; text-align: center; color: #64748b; border-top: 1px solid #e2e8f0; padding-top: 12px; }
            </style>
          </head>
          <body>
            ${bodyHtml}
            <div class="footer">Made with MoveProof • Free Renter Deposit Protection Printable</div>
          </body>
        </html>
      `;

      const { uri } = await Print.printToFileAsync({ html });
      await Share.share({
        title,
        url: uri
      });
    } catch (e) {
      console.error('Printable error:', e);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Free Renter Printables & Checklists</Text>
      <Text style={[styles.sectionSub, { color: colors.textSecondary }]}>
        No paywall required. Download, print, or share clean PDF templates with friends.
      </Text>

      <View style={styles.cardList}>
        {[
          { type: 'move_in', title: 'Move-In Condition Checklist PDF', desc: 'Comprehensive printable room inspection sheet.' },
          { type: 'cleaning', title: 'Move-Out Deep Cleaning Checklist PDF', desc: 'Ensure you pass move-out inspection without clean fees.' },
          { type: 'binder', title: 'Renter Deposit Binder Cover PDF', desc: 'Organize physical receipts, move-in sheets, and lease copies.' }
        ].map((item) => (
          <TouchableOpacity
            key={item.type}
            style={[styles.printableCard, { backgroundColor: colors.surface, borderColor: colors.border }]}
            onPress={() => handleGeneratePrintable(item.type)}
            activeOpacity={0.8}
          >
            <View style={[styles.iconCircle, { backgroundColor: colors.primaryLight }]}>
              <FileText size={20} color={colors.primary} />
            </View>
            <View style={styles.cardInfo}>
              <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>{item.title}</Text>
              <Text style={[styles.cardDesc, { color: colors.textSecondary }]}>{item.desc}</Text>
            </View>
            <Download size={20} color={colors.primary} />
          </TouchableOpacity>
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 16,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '800',
    marginBottom: 4,
  },
  sectionSub: {
    fontSize: 13,
    marginBottom: 14,
  },
  cardList: {
    gap: 12,
  },
  printableCard: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 14,
    borderRadius: 14,
    borderWidth: 1,
  },
  iconCircle: {
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },
  cardInfo: {
    flex: 1,
    marginRight: 8,
  },
  cardTitle: {
    fontSize: 14,
    fontWeight: '700',
  },
  cardDesc: {
    fontSize: 12,
    marginTop: 2,
  }
});
