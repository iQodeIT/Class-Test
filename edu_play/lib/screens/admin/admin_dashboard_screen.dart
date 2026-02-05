import 'package:edu_play/services/auth_service.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class AdminDashboardScreen extends StatelessWidget {
  const AdminDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Admin Panel'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => Provider.of<AuthService>(context, listen: false).logout(),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(24.0),
        children: [
          const _SchoolSummaryCard(
            schoolName: 'Lekki British School',
            schoolCode: 'LBS-2024-XP',
            totalStudents: 1200,
            totalTeachers: 85,
          ),
          const SizedBox(height: 32),
          const _AdminActionTile(
            title: 'Manage Teachers',
            icon: Icons.people,
            subtitle: 'Add, remove or edit teacher profiles',
          ),
          const SizedBox(height: 12),
          const _AdminActionTile(
            title: 'School Analytics',
            icon: Icons.bar_chart,
            subtitle: 'Overview of school-wide performance',
          ),
          const SizedBox(height: 12),
          const _AdminActionTile(
            title: 'Content Configuration',
            icon: Icons.settings_suggest,
            subtitle: 'Enable/disable subjects or grades',
          ),
          const SizedBox(height: 12),
          const _AdminActionTile(
            title: 'Subscription & Billing',
            icon: Icons.payments,
            subtitle: 'Manage school-wide premium status',
          ),
        ],
      ),
    );
  }
}

class _SchoolSummaryCard extends StatelessWidget {
  final String schoolName;
  final String schoolCode;
  final int totalStudents;
  final int totalTeachers;

  const _SchoolSummaryCard({
    required this.schoolName,
    required this.schoolCode,
    required this.totalStudents,
    required this.totalTeachers,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        color: AppColors.textDark,
        borderRadius: BorderRadius.circular(24),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(schoolName, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 22)),
          const SizedBox(height: 4),
          Row(
            children: [
              const Text('CODE: ', style: TextStyle(color: AppColors.textGrey, fontSize: 14)),
              Text(schoolCode, style: const TextStyle(color: AppColors.accentWarmYellow, fontWeight: FontWeight.bold, fontSize: 14)),
            ],
          ),
          const SizedBox(height: 24),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              _StatItem(label: 'Students', value: totalStudents.toString()),
              _StatItem(label: 'Teachers', value: totalTeachers.toString()),
            ],
          ),
        ],
      ),
    );
  }
}

class _StatItem extends StatelessWidget {
  final String label;
  final String value;

  const _StatItem({required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(value, style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 20)),
        Text(label, style: const TextStyle(color: AppColors.textGrey, fontSize: 12)),
      ],
    );
  }
}

class _AdminActionTile extends StatelessWidget {
  final String title;
  final String subtitle;
  final IconData icon;

  const _AdminActionTile({
    required this.title,
    required this.subtitle,
    required this.icon,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Container(
        padding: const EdgeInsets.all(8),
        decoration: BoxDecoration(
          color: AppColors.primarySkyBlue.withOpacity(0.1),
          borderRadius: BorderRadius.circular(10),
        ),
        child: Icon(icon, color: AppColors.primarySkyBlue),
      ),
      title: Text(title, style: const TextStyle(fontWeight: FontWeight.bold)),
      subtitle: Text(subtitle, style: const TextStyle(fontSize: 12)),
      trailing: const Icon(Icons.chevron_right),
      shape: RoundedRectangleBorder(
        side: BorderSide(color: Colors.grey.shade200),
        borderRadius: BorderRadius.circular(16),
      ),
    );
  }
}
