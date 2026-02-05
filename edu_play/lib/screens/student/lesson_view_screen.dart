import 'package:edu_play/models/lesson_model.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';

class LessonViewScreen extends StatelessWidget {
  final Lesson lesson;

  const LessonViewScreen({super.key, required this.lesson});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(lesson.title),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (lesson.mediaUrls.isNotEmpty)
              Container(
                height: 200,
                width: double.infinity,
                decoration: BoxDecoration(
                  color: Colors.grey.shade200,
                  borderRadius: BorderRadius.circular(16),
                ),
                child: const Icon(Icons.image, size: 64, color: AppColors.textGrey),
              ),
            const SizedBox(height: 24),
            Text(
              lesson.title,
              style: Theme.of(context).textTheme.displaySmall?.copyWith(fontSize: 24),
            ),
            const SizedBox(height: 16),
            Text(
              lesson.content,
              style: const TextStyle(fontSize: 16, height: 1.6),
            ),
            const SizedBox(height: 40),
            ElevatedButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Mark as Complete'),
            ),
          ],
        ),
      ),
    );
  }
}
