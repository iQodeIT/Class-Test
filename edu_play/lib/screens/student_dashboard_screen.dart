import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/user_provider.dart';
import '../services/cms_mock_service.dart';
import '../config/app_constants.dart';
import 'lesson_detail_screen.dart';
import 'quiz_screen.dart';

class StudentDashboardScreen extends StatelessWidget {
  const StudentDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF0F9FF), // Very light blue
      appBar: AppBar(
        title: const Text('EduPlay Student', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: AppColors.primarySkyBlue,
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => Provider.of<UserProvider>(context, listen: false).logout(),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildProfileSection(),
            const SizedBox(height: 24),
            _buildDailyMissions(),
            const SizedBox(height: 24),
            _buildQuickActions(context),
          ],
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        selectedItemColor: AppColors.primarySkyBlue,
        unselectedItemColor: Colors.grey,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Home'),
          BottomNavigationBarItem(icon: Icon(Icons.play_arrow), label: 'Play'),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: 'Profile'),
        ],
      ),
    );
  }

  Widget _buildProfileSection() {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        children: [
          Row(
            children: [
              const CircleAvatar(
                radius: 30,
                backgroundColor: AppColors.accentWarmYellow,
                child: Icon(Icons.face, size: 40, color: Colors.white),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Hello, Tunde!',
                      style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                    const Text('Level 5 Explorer', style: TextStyle(color: Colors.grey)),
                  ],
                ),
              ),
              Column(
                children: [
                  const Icon(Icons.local_fire_department, color: Colors.orange),
                  const Text('5 Day Streak', style: TextStyle(fontWeight: FontWeight.bold)),
                ],
              ),
            ],
          ),
          const SizedBox(height: 20),
          const Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('XP Progress', style: TextStyle(fontWeight: FontWeight.bold)),
              Text('450 / 1000 XP'),
            ],
          ),
          const SizedBox(height: 8),
          LinearProgressIndicator(
            value: 0.45,
            backgroundColor: Colors.grey[200],
            color: AppColors.primarySkyBlue,
            minHeight: 12,
            borderRadius: BorderRadius.circular(6),
          ),
        ],
      ),
    );
  }

  Widget _buildDailyMissions() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Daily Missions',
          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 12),
        _buildMissionCard('Math Master', 'Complete 2 Math quizzes', 0.5),
        _buildMissionCard('Reading Hero', 'Read 1 English lesson', 1.0),
      ],
    );
  }

  Widget _buildMissionCard(String title, String subtitle, double progress) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      child: ListTile(
        leading: CircleAvatar(
          backgroundColor: progress == 1.0 ? AppColors.secondaryGreen : AppColors.accentWarmYellow,
          child: Icon(
            progress == 1.0 ? Icons.check : Icons.star,
            color: Colors.white,
          ),
        ),
        title: Text(title, style: const TextStyle(fontWeight: FontWeight.bold)),
        subtitle: Text(subtitle),
        trailing: progress == 1.0
            ? const Text('Done!', style: TextStyle(color: AppColors.secondaryGreen, fontWeight: FontWeight.bold))
            : SizedBox(
                width: 40,
                height: 40,
                child: CircularProgressIndicator(
                  value: progress,
                  strokeWidth: 4,
                  backgroundColor: Colors.grey[200],
                  color: AppColors.primarySkyBlue,
                ),
              ),
      ),
    );
  }

  Widget _buildQuickActions(BuildContext context) {
    return GridView.count(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      crossAxisCount: 2,
      crossAxisSpacing: 16,
      mainAxisSpacing: 16,
      children: [
        _buildActionCard(context, 'Lessons', Icons.book, AppColors.primarySkyBlue, () {
          final lesson = CMSMockService.getLesson('t1');
          Navigator.push(context, MaterialPageRoute(builder: (context) => LessonDetailScreen(lesson: lesson)));
        }),
        _buildActionCard(context, 'Quizzes', Icons.quiz, AppColors.secondaryCoralRed, () {
          final quizzes = CMSMockService.getQuizzes('t1');
          Navigator.push(context, MaterialPageRoute(builder: (context) => QuizScreen(quizzes: quizzes)));
        }),
        _buildActionCard(context, 'Games', Icons.videogame_asset, AppColors.secondaryGreen, () {
          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Games coming soon!')));
        }),
        _buildActionCard(context, 'Badges', Icons.emoji_events, AppColors.accentWarmYellow, () {
          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Badges coming soon!')));
        }),
      ],
    );
  }

  Widget _buildActionCard(BuildContext context, String title, IconData icon, Color color, VoidCallback onTap) {
    return InkWell(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(20),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 48, color: Colors.white),
            const SizedBox(height: 8),
            Text(
              title,
              style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
            ),
          ],
        ),
      ),
    );
  }
}