import 'package:flutter/material.dart';

class AdminDashboardScreen extends StatelessWidget {
  const AdminDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      appBar: AppBar(title: Text('Admin Dashboard')),
      body: Center(
        child: Text('Admin Dashboard'),
      ),
    );
  }
}