import 'package:edu_play/services/auth_service.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class ParentDashboardScreen extends StatelessWidget {
  const ParentDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Parent Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => Provider.of<AuthService>(context, listen: false).logout(),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const _ChildOverviewCard(
              name: 'Tunde',
              grade: 'Primary 4',
              completionRate: 0.75,
              lastActive: 'Today, 10:30 AM',
            ),
            const SizedBox(height: 32),
            Text('Learning Progress', style: Theme.of(context).textTheme.titleLarge),
            const SizedBox(height: 16),
            const _ProgressItem(subject: 'Mathematics', progress: 0.8),
            const SizedBox(height: 12),
            const _ProgressItem(subject: 'English Language', progress: 0.6),
            const SizedBox(height: 12),
            const _ProgressItem(subject: 'Basic Science', progress: 0.4),
            const Spacer(),
            ElevatedButton.icon(
              onPressed: () {},
              icon: const Icon(Icons.picture_as_pdf),
              label: const Text('Export Monthly Report'),
            ),
          ],
        ),
      ),
    );
  }
}

class _ChildOverviewCard extends StatelessWidget {
  final String name;
  final String grade;
  final double completionRate;
  final String lastActive;

  const _ChildOverviewCard({
    required this.name,
    required this.grade,
    required this.completionRate,
    required this.lastActive,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [AppColors.primarySkyBlue, Color(0xFF60A5FA)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(24),
      ),
      child: Column(
        children: [
          Row(
            children: [
              const CircleAvatar(
                radius: 30,
                backgroundColor: Colors.white,
                child: Icon(Icons.person, size: 40, color: AppColors.primarySkyBlue),
              ),
              const SizedBox(width: 16),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    name,
                    style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 20),
                  ),
                  Text(
                    grade,
                    style: TextStyle(color: Colors.white.withOpacity(0.9), fontSize: 14),
                  ),
                ],
              ),
            ],
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('Overall Completion', style: TextStyle(color: Colors.white)),
              Text('${(completionRate * 100).toInt()}%', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 8),
          LinearProgressIndicator(
            value: completionRate,
            backgroundColor: Colors.white.withOpacity(0.3),
            color: AppColors.accentWarmYellow,
            borderRadius: BorderRadius.circular(10),
          ),
          const SizedBox(height: 12),
          Text(
            'Last active: $lastActive',
            style: TextStyle(color: Colors.white.withOpacity(0.8), fontSize: 12),
          ),
        ],
      ),
    );
  }
}

class _ProgressItem extends StatelessWidget {
  final String subject;
  final double progress;

  const _ProgressItem({required this.subject, required this.progress});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(subject, style: const TextStyle(fontWeight: FontWeight.w500)),
            Text('${(progress * 100).toInt()}%'),
          ],
        ),
        const SizedBox(height: 6),
        LinearProgressIndicator(
          value: progress,
          backgroundColor: Colors.grey.shade200,
          color: AppColors.primarySkyBlue,
          borderRadius: BorderRadius.circular(10),
        ),
      ],
    );
  }
}
