import 'package:flutter/material.dart';
import '../models/quiz_model.dart';
import '../config/app_constants.dart';

class QuizScreen extends StatefulWidget {
  final List<QuizItem> quizzes;

  const QuizScreen({super.key, required this.quizzes});

  @override
  State<QuizScreen> createState() => _QuizScreenState();
}

class _QuizScreenState extends State<QuizScreen> {
  int _currentIndex = 0;
  String? _selectedAnswer;
  bool _isAnswered = false;
  int _score = 0;

  void _submitAnswer(String answer) {
    if (_isAnswered) return;

    setState(() {
      _selectedAnswer = answer;
      _isAnswered = true;
      if (answer == widget.quizzes[_currentIndex].correctAnswer) {
        _score++;
      }
    });
  }

  void _nextQuestion() {
    if (_currentIndex < widget.quizzes.length - 1) {
      setState(() {
        _currentIndex++;
        _selectedAnswer = null;
        _isAnswered = false;
      });
    } else {
      _showResult();
    }
  }

  void _showResult() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        title: const Text('Quiz Completed!'),
        content: Text('Your score: $_score / ${widget.quizzes.length}'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context); // Pop dialog
              Navigator.pop(context); // Pop QuizScreen
            },
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final quiz = widget.quizzes[_currentIndex];

    return Scaffold(
      appBar: AppBar(
        title: Text('Quiz: ${_currentIndex + 1}/${widget.quizzes.length}'),
        backgroundColor: AppColors.secondaryCoralRed,
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              quiz.question,
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 32),
            ...quiz.options.map((option) => _buildOption(option)),
            const Spacer(),
            if (_isAnswered)
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: _selectedAnswer == quiz.correctAnswer
                      ? Colors.green[100]
                      : Colors.red[100],
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      _selectedAnswer == quiz.correctAnswer ? 'Correct!' : 'Incorrect!',
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        color: _selectedAnswer == quiz.correctAnswer ? Colors.green : Colors.red,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(quiz.explanation),
                  ],
                ),
              ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: _isAnswered ? _nextQuestion : null,
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 50),
                backgroundColor: AppColors.secondaryCoralRed,
                foregroundColor: Colors.white,
              ),
              child: Text(_currentIndex < widget.quizzes.length - 1 ? 'Next Question' : 'See Results'),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildOption(String option) {
    final quiz = widget.quizzes[_currentIndex];
    bool isSelected = _selectedAnswer == option;
    bool isCorrect = quiz.correctAnswer == option;

    Color borderColor = Colors.grey[300]!;
    Color bgColor = Colors.white;

    if (_isAnswered) {
      if (isCorrect) {
        borderColor = Colors.green;
        bgColor = Colors.green[50]!;
      } else if (isSelected) {
        borderColor = Colors.red;
        bgColor = Colors.red[50]!;
      }
    } else if (isSelected) {
      borderColor = AppColors.secondaryCoralRed;
    }

    return Padding(
      padding: const EdgeInsets.only(bottom: 12.0),
      child: InkWell(
        onTap: () => _submitAnswer(option),
        child: Container(
          width: double.infinity,
          padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
          decoration: BoxDecoration(
            color: bgColor,
            border: Border.all(color: borderColor, width: 2),
            borderRadius: BorderRadius.circular(15),
          ),
          child: Text(
            option,
            style: const TextStyle(fontSize: 18),
          ),
        ),
      ),
    );
  }
}
