import 'package:edu_play/models/lesson_model.dart';
import 'package:edu_play/models/quiz_model.dart';
import 'package:edu_play/data/mock_data.dart';
import 'package:edu_play/screens/student/lesson_view_screen.dart';
import 'package:edu_play/screens/student/quiz_screen.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';

class SubjectLessonsScreen extends StatelessWidget {
  final Subject subject;

  const SubjectLessonsScreen({super.key, required this.subject});

  @override
  Widget build(BuildContext context) {
    final topics = mockTopics.where((t) => t.subjectId == subject.id).toList();

    return Scaffold(
      appBar: AppBar(
        title: Text(subject.name),
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(24),
        itemCount: topics.length,
        itemBuilder: (context, index) {
          final topic = topics[index];
          return _TopicExpansionTile(topic: topic, subjectName: subject.name);
        },
      ),
    );
  }
}

class _TopicExpansionTile extends StatelessWidget {
  final Topic topic;
  final String subjectName;

  const _TopicExpansionTile({required this.topic, required this.subjectName});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
        side: BorderSide(color: Colors.grey.shade200),
      ),
      margin: const EdgeInsets.only(bottom: 16),
      child: ExpansionTile(
        title: Text(topic.name, style: const TextStyle(fontWeight: FontWeight.bold)),
        leading: const Icon(Icons.folder_open, color: AppColors.primarySkyBlue),
        children: [
          _LessonTile(
            title: 'Introduction to ${topic.name}',
            isCompleted: true,
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => LessonViewScreen(
                    lesson: Lesson(
                      id: 'l1',
                      topicId: topic.id,
                      title: 'Introduction to ${topic.name}',
                      content: 'In this lesson, we will learn the basics of ${topic.name}. '
                          'It is a fundamental concept in $subjectName and will help you solve many problems. '
                          'Pay close attention to the examples!',
                      mediaUrls: ['https://example.com/image.png'],
                    ),
                  ),
                ),
              );
            },
          ),
          _LessonTile(
            title: 'Advanced ${topic.name}',
            isCompleted: false,
            onTap: () {},
          ),
          _QuizTile(
            title: '${topic.name} Challenge',
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => QuizScreen(
                    quiz: Quiz(
                      id: 'q1',
                      lessonId: 'l1',
                      questions: [
                        QuizQuestion(
                          id: 'qq1',
                          question: 'What is 5 + 7?',
                          options: ['10', '11', '12', '13'],
                          correctAnswerIndex: 2,
                          explanation: '5 plus 7 equals 12.',
                        ),
                        QuizQuestion(
                          id: 'qq2',
                          question: 'If you have 3 apples and buy 4 more, how many do you have?',
                          options: ['6', '7', '8', '9'],
                          correctAnswerIndex: 1,
                          explanation: '3 + 4 = 7.',
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

class _LessonTile extends StatelessWidget {
  final String title;
  final bool isCompleted;
  final VoidCallback onTap;

  const _LessonTile({
    required this.title,
    required this.isCompleted,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(title, style: const TextStyle(fontSize: 14)),
      trailing: Icon(
        isCompleted ? Icons.check_circle : Icons.play_circle_outline,
        color: isCompleted ? AppColors.secondaryGreen : AppColors.primarySkyBlue,
      ),
      onTap: onTap,
    );
  }
}

class _QuizTile extends StatelessWidget {
  final String title;
  final VoidCallback onTap;

  const _QuizTile({required this.title, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(title, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold)),
      leading: const Icon(Icons.help_outline, color: AppColors.accentWarmYellow),
      trailing: const Icon(Icons.star, color: AppColors.accentWarmYellow),
      onTap: onTap,
    );
  }
}
