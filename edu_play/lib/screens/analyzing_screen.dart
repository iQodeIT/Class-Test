import 'package:edu_play/models/project.dart';
import 'package:edu_play/services/newell_ai_service.dart';
import 'package:edu_play/services/project_provider.dart';
import 'package:edu_play/utils/app_theme.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class AnalyzingScreen extends StatefulWidget {
  final Project project;

  const AnalyzingScreen({super.key, required this.project});

  @override
  State<AnalyzingScreen> createState() => _AnalyzingScreenState();
}

class _AnalyzingScreenState extends State<AnalyzingScreen> {
  int _currentStep = 0;
  final List<String> _steps = [
    "Uploading audio to Newell AI...",
    "Transcribing speech to text...",
    "Identifying 'Smart Words' & generating definitions..."
  ];

  @override
  void initState() {
    super.initState();
    _startAnalysis();
  }

  Future<void> _startAnalysis() async {
    final aiService = NewellAIService();

    // Step 0 -> 1
    await Future.delayed(const Duration(seconds: 2));
    if (mounted) setState(() => _currentStep = 1);

    // Actually call the service
    final transcript = await aiService.processMedia(widget.project.audioPath);

    // Step 1 -> 2
    if (mounted) setState(() => _currentStep = 2);
    await Future.delayed(const Duration(seconds: 2));

    // Save to project
    widget.project.transcript = transcript;
    if (mounted) {
      await context.read<ProjectProvider>().updateProject(widget.project);
      // Navigate to Editor
      if (mounted) {
        // We'll implement EditorScreen next
        Navigator.pushReplacementNamed(context, '/editor', arguments: widget.project);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.deepCharcoal,
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(40.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const CircularProgressIndicator(color: AppTheme.linguaGold, strokeWidth: 6),
              const SizedBox(height: 40),
              Text(
                "Analyzing your content...",
                style: Theme.of(context).textTheme.displayMedium,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 24),
              ...List.generate(_steps.length, (index) {
                final isActive = index == _currentStep;
                final isDone = index < _currentStep;
                return Padding(
                  padding: const EdgeInsets.symmetric(vertical: 8.0),
                  child: Row(
                    children: [
                      Icon(
                        isDone ? Icons.check_circle : (isActive ? Icons.sync : Icons.radio_button_unchecked),
                        color: isDone ? Colors.green : (isActive ? AppTheme.linguaGold : AppTheme.slateGray),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Text(
                          _steps[index],
                          style: TextStyle(
                            color: isActive ? Colors.white : AppTheme.slateGray,
                            fontWeight: isActive ? FontWeight.bold : FontWeight.normal,
                          ),
                        ),
                      ),
                    ],
                  ),
                );
              }),
            ],
          ),
        ),
      ),
    );
  }
}
