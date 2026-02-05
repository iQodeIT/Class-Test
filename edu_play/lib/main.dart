import 'package:edu_play/main_wrapper.dart';
import 'package:edu_play/services/auth_service.dart';
import 'package:edu_play/styles/app_theme.dart';
import 'package:flutter/material.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:provider/provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialize Hive for local storage
  await Hive.initFlutter();
  await Hive.openBox('settings'); // Box for app settings and user session

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthService()),
      ],
      child: MaterialApp(
        title: 'EduPlay',
        theme: AppTheme.lightTheme,
        home: const MainWrapper(),
        debugShowCheckedModeBanner: false,
      ),
    );
  }
}