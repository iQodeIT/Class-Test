import 'package:flutter/material.dart';

class ParentDashboardScreen extends StatelessWidget {
  const ParentDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      appBar: AppBar(title: Text('Parent Dashboard')),
      body: Center(
        child: Text('Parent Dashboard'),
      ),
    );
  }
}