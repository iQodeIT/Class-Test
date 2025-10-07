import 'package:flutter/material.dart';

class TeacherDashboardScreen extends StatelessWidget {
  const TeacherDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      appBar: AppBar(title: Text('Teacher Dashboard')),
      body: Center(
        child: Text('Teacher Dashboard'),
      ),
    );
  }
}