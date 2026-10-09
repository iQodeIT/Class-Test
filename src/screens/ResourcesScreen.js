import React from 'react';
import { StyleSheet, View, Text, SafeAreaView, ScrollView, TouchableOpacity, Linking } from 'react-native';
import { BookOpen, Shield, Scale, CheckSquare, ExternalLink } from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import FreePrintablesSection from '../components/FreePrintablesSection';

export default function ResourcesScreen({ navigateTo }) {
  const { colors } = useApp();

  const ARTICLES = [
    {
      id: '1',
      title: 'Security Deposit Basics & Renter Rights',
      category: 'Renter Rights',
      readTime: '4 min read',
      summary: 'Learn what landlords can legally deduct, statutory return deadlines, and how to protect your money.',
      icon: Shield
    },
    {
      id: '2',
      title: 'What Landlords Can and Can’t Deduct',
      category: 'Wear vs Damage',
      readTime: '5 min read',
      summary: 'Normal wear and tear (faded paint, carpet depression) cannot be deducted. Know the legal boundaries.',
      icon: Scale
    },
    {
      id: '3',
      title: 'How to Write an Effective Demand Letter',
      category: 'Legal Recovery',
      readTime: '3 min read',
      summary: 'Step-by-step guide to writing a pre-suit demand letter that gets your deposit returned without court.',
      icon: BookOpen
    }
  ];

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      <View style={styles.header}>
        <Text style={[styles.headerTitle, { color: colors.textPrimary }]}>Resources & Guides</Text>
        <Text style={[styles.headerSub, { color: colors.textSecondary }]}>Legal basics, guides, and free printables</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Articles List */}
        <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Rental Educational Guides</Text>
        <View style={styles.articlesList}>
          {ARTICLES.map((article) => {
            const IconComp = article.icon;
            return (
              <View
                key={article.id}
                style={[styles.articleCard, { backgroundColor: colors.surface, borderColor: colors.border }]}
              >
                <View style={styles.articleHeader}>
                  <View style={[styles.iconBox, { backgroundColor: colors.primaryLight }]}>
                    <IconComp size={20} color={colors.primary} />
                  </View>

                  <View style={styles.articleMeta}>
                    <Text style={[styles.categoryText, { color: colors.primary }]}>{article.category}</Text>
                    <Text style={[styles.readTimeText, { color: colors.textMuted }]}>{article.readTime}</Text>
                  </View>
                </View>

                <Text style={[styles.articleTitle, { color: colors.textPrimary }]}>{article.title}</Text>
                <Text style={[styles.articleSummary, { color: colors.textSecondary }]}>{article.summary}</Text>
              </View>
            );
          })}
        </View>

        {/* Free Printables Section */}
        <FreePrintablesSection />

        {/* Disclaimer */}
        <View style={[styles.disclaimerBox, { backgroundColor: colors.surfaceVariant }]}>
          <Text style={[styles.disclaimerText, { color: colors.textSecondary }]}>
            Disclaimer: Educational resources provided in MoveProof are for general informational purposes only and do not constitute legal advice.
          </Text>
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
  sectionTitle: {
    fontSize: 18,
    fontWeight: '800',
    marginBottom: 12,
  },
  articlesList: {
    gap: 12,
    marginBottom: 20,
  },
  articleCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
  },
  articleHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  iconBox: {
    width: 36,
    height: 36,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
  },
  articleMeta: {
    alignItems: 'flex-end',
  },
  categoryText: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
  },
  readTimeText: {
    fontSize: 10,
  },
  articleTitle: {
    fontSize: 16,
    fontWeight: '700',
    marginBottom: 6,
  },
  articleSummary: {
    fontSize: 13,
    lineHeight: 18,
  },
  disclaimerBox: {
    padding: 14,
    borderRadius: 12,
    marginTop: 10,
  },
  disclaimerText: {
    fontSize: 11,
    lineHeight: 16,
    textAlign: 'center',
  }
});
