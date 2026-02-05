import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'screens/onboarding_screen.dart';
import 'screens/student_dashboard_screen.dart';
import 'screens/parent_dashboard_screen.dart';
import 'screens/teacher_dashboard_screen.dart';
import 'screens/admin_dashboard_screen.dart';
import 'services/user_provider.dart';

class MainWrapper extends StatelessWidget {
  const MainWrapper({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<UserProvider>(
      builder: (context, userProvider, child) {
        final user = userProvider.user;

        if (user == null) {
          return const OnboardingScreen();
        }

        switch (user.role) {
          case 'Student':
            return const StudentDashboardScreen();
          case 'Parent':
            return const ParentDashboardScreen();
          case 'Teacher':
            return const TeacherDashboardScreen();
          case 'Admin':
            return const AdminDashboardScreen();
          default:
            return const OnboardingScreen();
        }
      },
    );
  }
}
