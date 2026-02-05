import 'package:edu_play/MainWrapper.dart';
import 'package:edu_play/services/user_provider.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'services/notification_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await NotificationService.initialize();
  await Hive.initFlutter();
  await Hive.openBox('settings');
  await Hive.openBox('lessons');

  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => UserProvider()),
      ],
      child: const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'EduPlay',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF3B82F6), // Primary: Sky Blue
          primary: const Color(0xFF3B82F6),
          secondary: const Color(0xFFFACC15), // Accent: Warm Yellow
          error: const Color(0xFFF87171), // Secondary: Coral Red
        ),
        textTheme: GoogleFonts.nunitoTextTheme(
          Theme.of(context).textTheme,
        ),
        useMaterial3: true,
      ),
      home: const MainWrapper(),
      debugShowCheckedModeBanner: false,
    );
  }
}