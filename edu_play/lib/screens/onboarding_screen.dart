import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/user_provider.dart';
import '../config/app_constants.dart';

class OnboardingScreen extends StatelessWidget {
  const OnboardingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.school_rounded,
                size: 100,
                color: AppColors.primarySkyBlue,
              ),
              const SizedBox(height: 24),
              const Text(
                'Welcome to EduPlay!',
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                  color: AppColors.primarySkyBlue,
                ),
              ),
              const SizedBox(height: 16),
              const Text(
                'Gamified learning for Nigerian Students. Please select your role to continue.',
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 16, color: Colors.grey),
              ),
              const SizedBox(height: 48),
              _buildRoleButton(context, 'Student', AppColors.primarySkyBlue),
              const SizedBox(height: 12),
              _buildRoleButton(context, 'Parent', AppColors.secondaryGreen),
              const SizedBox(height: 12),
              _buildRoleButton(context, 'Teacher', AppColors.accentWarmYellow),
              const SizedBox(height: 12),
              _buildRoleButton(context, 'Admin', AppColors.secondaryCoralRed),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildRoleButton(BuildContext context, String role, Color color) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          backgroundColor: color,
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        onPressed: () {
          Provider.of<UserProvider>(context, listen: false).setRole(role);
        },
        child: Text(
          'Join as $role',
          style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
      ),
    );
  }
}