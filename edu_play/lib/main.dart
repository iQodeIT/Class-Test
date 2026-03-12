import 'package:edu_play/models/project.dart';
import 'package:edu_play/screens/analyzing_screen.dart';
import 'package:edu_play/screens/dashboard_screen.dart';
import 'package:edu_play/screens/editor_screen.dart';
import 'package:edu_play/screens/export_screen.dart';
import 'package:edu_play/services/project_provider.dart';
import 'package:edu_play/utils/app_theme.dart';
import 'package:flutter/material.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:provider/provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialize Hive
  await Hive.initFlutter();

  // Register Adapters
  Hive.registerAdapter(ProjectAdapter());
  Hive.registerAdapter(TranscriptionWordAdapter());
  Hive.registerAdapter(AspectRatioTypeAdapter());

  final projectProvider = ProjectProvider();
  await projectProvider.init();

  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: projectProvider),
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
      title: 'LinguaClip',
      theme: AppTheme.darkTheme,
      home: const DashboardScreen(),
      debugShowCheckedModeBanner: false,
      onGenerateRoute: (settings) {
        if (settings.name == '/analyzing') {
          final project = settings.arguments as Project;
          return MaterialPageRoute(builder: (_) => AnalyzingScreen(project: project));
        }
        if (settings.name == '/editor') {
          final project = settings.arguments as Project;
          return MaterialPageRoute(builder: (_) => EditorScreen(project: project));
        }
        if (settings.name == '/export') {
          final project = settings.arguments as Project;
          return MaterialPageRoute(builder: (_) => ExportScreen(project: project));
        }
        return null;
      },
    );
  }
}

class PlaceholderScreen extends StatelessWidget {
  const PlaceholderScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'LINGUACLIP',
              style: Theme.of(context).textTheme.displayLarge,
            ),
            const SizedBox(height: 20),
            const CircularProgressIndicator(color: AppTheme.linguaGold),
          ],
        ),
      ),
    );
  }
}
