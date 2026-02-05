import 'package:edu_play/services/auth_service.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final user = Provider.of<AuthService>(context).currentUser!;

    return Scaffold(
      appBar: AppBar(title: const Text('My Profile')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            const CircleAvatar(
              radius: 60,
              backgroundColor: AppColors.primarySkyBlue,
              child: Icon(Icons.person, size: 80, color: Colors.white),
            ),
            const SizedBox(height: 16),
            Text(user.name, style: Theme.of(context).textTheme.displaySmall),
            Text(user.grade ?? '', style: const TextStyle(color: AppColors.textGrey, fontSize: 18)),
            const SizedBox(height: 32),
            _ProfileStatRow(xp: user.xp ?? 0, streak: user.streak ?? 0),
            const SizedBox(height: 32),
            const Divider(),
            _ProfileMenuTile(title: 'Avatar Customization', icon: Icons.face, onTap: () {}),
            _ProfileMenuTile(title: 'My Badges', icon: Icons.emoji_events, onTap: () {}),
            _ProfileMenuTile(title: 'Settings', icon: Icons.settings, onTap: () {}),
            _ProfileMenuTile(
              title: 'Logout',
              icon: Icons.logout,
              color: AppColors.secondaryCoralRed,
              onTap: () => Provider.of<AuthService>(context, listen: false).logout(),
            ),
          ],
        ),
      ),
    );
  }
}

class _ProfileStatRow extends StatelessWidget {
  final int xp;
  final int streak;

  const _ProfileStatRow({required this.xp, required this.streak});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        _StatItem(label: 'Total XP', value: xp.toString(), icon: Icons.bolt, color: AppColors.accentWarmYellow),
        _StatItem(label: 'Day Streak', value: streak.toString(), icon: Icons.local_fire_department, color: AppColors.secondaryCoralRed),
      ],
    );
  }
}

class _StatItem extends StatelessWidget {
  final String label;
  final String value;
  final IconData icon;
  final Color color;

  const _StatItem({required this.label, required this.value, required this.icon, required this.color});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Icon(icon, color: color, size: 32),
        const SizedBox(height: 8),
        Text(value, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 24)),
        Text(label, style: const TextStyle(color: AppColors.textGrey)),
      ],
    );
  }
}

class _ProfileMenuTile extends StatelessWidget {
  final String title;
  final IconData icon;
  final VoidCallback onTap;
  final Color? color;

  const _ProfileMenuTile({required this.title, required this.icon, required this.onTap, this.color});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon, color: color ?? AppColors.primarySkyBlue),
      title: Text(title, style: TextStyle(color: color)),
      trailing: const Icon(Icons.chevron_right),
      onTap: onTap,
    );
  }
}
