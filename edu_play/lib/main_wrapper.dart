import 'package:edu_play/models/user_model.dart';
import 'package:edu_play/screens/admin/admin_dashboard_screen.dart';
import 'package:edu_play/screens/onboarding_screen.dart';
import 'package:edu_play/screens/parent/parent_dashboard_screen.dart';
import 'package:edu_play/screens/student/student_dashboard_screen.dart';
import 'package:edu_play/screens/teacher/teacher_dashboard_screen.dart';
import 'package:edu_play/services/auth_service.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class MainWrapper extends StatelessWidget {
  const MainWrapper({super.key});

  @override
  Widget build(BuildContext context) {
    final authService = Provider.of<AuthService>(context);

    if (!authService.isAuthenticated) {
      return const OnboardingScreen();
    }

    final user = authService.currentUser!;
    switch (user.role) {
      case UserRole.student:
        return const StudentDashboardScreen();
      case UserRole.parent:
        return const ParentDashboardScreen();
      case UserRole.teacher:
        return const TeacherDashboardScreen();
      case UserRole.admin:
        return const AdminDashboardScreen();
    }
  }
}
