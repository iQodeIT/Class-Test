import 'package:edu_play/models/user_model.dart';
import 'package:edu_play/services/auth_service.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class OnboardingScreen extends StatefulWidget {
  const OnboardingScreen({super.key});

  @override
  State<OnboardingScreen> createState() => _OnboardingScreenState();
}

class _OnboardingScreenState extends State<OnboardingScreen> {
  UserRole? _selectedRole;

  void _handleJoin(BuildContext context) {
    if (_selectedRole == null) return;

    final authService = Provider.of<AuthService>(context, listen: false);

    // Create a mock user based on selected role
    final mockUser = UserModel(
      id: 'user_123',
      email: 'test@eduplay.ng',
      name: 'Tunde',
      role: _selectedRole!,
      xp: _selectedRole == UserRole.student ? 100 : null,
      streak: _selectedRole == UserRole.student ? 5 : null,
      grade: _selectedRole == UserRole.student ? 'Primary 4' : null,
    );

    authService.login(mockUser);

    // Navigation will be handled by a wrapper or direct push
    // For now, let's just push to the correct dashboard
    _navigateToDashboard(context, _selectedRole!);
  }

  void _navigateToDashboard(BuildContext context, UserRole role) {
    // Auth state change in Provider will trigger MainWrapper to rebuild
    // No need to manually navigate if we use a wrapper, but let's just make sure it triggers.
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              const SizedBox(height: 40),
              Text(
                'Welcome to EduPlay!',
                style: Theme.of(context).textTheme.displayMedium,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 16),
              const Text(
                'Gamified learning designed for Nigerian students. Choose your role to get started.',
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 16, color: AppColors.textGrey),
              ),
              const SizedBox(height: 48),
              Expanded(
                child: GridView.count(
                  crossAxisCount: 2,
                  mainAxisSpacing: 16,
                  crossAxisSpacing: 16,
                  children: [
                    _RoleCard(
                      title: 'Student',
                      icon: Icons.school,
                      isSelected: _selectedRole == UserRole.student,
                      onTap: () => setState(() => _selectedRole = UserRole.student),
                    ),
                    _RoleCard(
                      title: 'Parent',
                      icon: Icons.family_restroom,
                      isSelected: _selectedRole == UserRole.parent,
                      onTap: () => setState(() => _selectedRole = UserRole.parent),
                    ),
                    _RoleCard(
                      title: 'Teacher',
                      icon: Icons.person,
                      isSelected: _selectedRole == UserRole.teacher,
                      onTap: () => setState(() => _selectedRole = UserRole.teacher),
                    ),
                    _RoleCard(
                      title: 'Admin',
                      icon: Icons.admin_panel_settings,
                      isSelected: _selectedRole == UserRole.admin,
                      onTap: () => setState(() => _selectedRole = UserRole.admin),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _selectedRole != null ? () => _handleJoin(context) : null,
                child: const Text('Get Started'),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}

class _RoleCard extends StatelessWidget {
  final String title;
  final IconData icon;
  final bool isSelected;
  final VoidCallback onTap;

  const _RoleCard({
    required this.title,
    required this.icon,
    required this.isSelected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        decoration: BoxDecoration(
          color: isSelected ? AppColors.primarySkyBlue : Colors.white,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: isSelected ? AppColors.primarySkyBlue : Colors.grey.shade300,
            width: 2,
          ),
          boxShadow: isSelected
              ? [BoxShadow(color: AppColors.primarySkyBlue.withOpacity(0.3), blurRadius: 8, offset: const Offset(0, 4))]
              : null,
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              size: 48,
              color: isSelected ? Colors.white : AppColors.primarySkyBlue,
            ),
            const SizedBox(height: 12),
            Text(
              title,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 18,
                color: isSelected ? Colors.white : AppColors.textDark,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
