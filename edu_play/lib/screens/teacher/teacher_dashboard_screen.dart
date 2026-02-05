import 'package:edu_play/services/auth_service.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class TeacherDashboardScreen extends StatelessWidget {
  const TeacherDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Teacher Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => Provider.of<AuthService>(context, listen: false).logout(),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Class Performance (JSS 1)', style: Theme.of(context).textTheme.titleLarge),
            const SizedBox(height: 16),
            const _AnalyticsCard(
              title: 'Avg. Quiz Score',
              value: '82%',
              trend: '+5%',
              color: AppColors.secondaryGreen,
            ),
            const SizedBox(height: 12),
            const _AnalyticsCard(
              title: 'Active Students',
              value: '24/30',
              trend: 'Stable',
              color: AppColors.primarySkyBlue,
            ),
            const SizedBox(height: 32),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Recent Assignments', style: Theme.of(context).textTheme.titleLarge),
                TextButton(onPressed: () {}, child: const Text('View All')),
              ],
            ),
            const SizedBox(height: 12),
            const _AssignmentTile(
              title: 'Algebra Intro',
              dueDate: 'Due Tomorrow',
              submittedCount: 18,
              totalCount: 30,
            ),
            const SizedBox(height: 12),
            const _AssignmentTile(
              title: 'Grammar: Nouns',
              dueDate: 'Due in 3 days',
              submittedCount: 5,
              totalCount: 30,
            ),
            const SizedBox(height: 32),
            ElevatedButton(
              onPressed: () {},
              child: const Text('Create New Assignment'),
            ),
          ],
        ),
      ),
    );
  }
}

class _AnalyticsCard extends StatelessWidget {
  final String title;
  final String value;
  final String trend;
  final Color color;

  const _AnalyticsCard({
    required this.title,
    required this.value,
    required this.trend,
    required this.color,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: color.withOpacity(0.3)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(title, style: const TextStyle(color: AppColors.textGrey, fontSize: 14)),
              const SizedBox(height: 4),
              Text(value, style: TextStyle(color: color, fontWeight: FontWeight.bold, fontSize: 24)),
            ],
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
            decoration: BoxDecoration(
              color: color,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(
              trend,
              style: const TextStyle(color: Colors.white, fontSize: 12, fontWeight: FontWeight.bold),
            ),
          ),
        ],
      ),
    );
  }
}

class _AssignmentTile extends StatelessWidget {
  final String title;
  final String dueDate;
  final int submittedCount;
  final int totalCount;

  const _AssignmentTile({
    required this.title,
    required this.dueDate,
    required this.submittedCount,
    required this.totalCount,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: Colors.grey.shade200),
      ),
      child: Row(
        children: [
          const Icon(Icons.assignment, color: AppColors.primarySkyBlue),
          const SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(title, style: const TextStyle(fontWeight: FontWeight.bold)),
                Text(dueDate, style: const TextStyle(color: AppColors.secondaryCoralRed, fontSize: 12)),
              ],
            ),
          ),
          Text('$submittedCount/$totalCount', style: const TextStyle(fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }
}
