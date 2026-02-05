import 'package:edu_play/models/quiz_model.dart';
import 'package:edu_play/styles/app_colors.dart';
import 'package:flutter/material.dart';

class QuizScreen extends StatefulWidget {
  final Quiz quiz;

  const QuizScreen({super.key, required this.quiz});

  @override
  State<QuizScreen> createState() => _QuizScreenState();
}

class _QuizScreenState extends State<QuizScreen> {
  int _currentQuestionIndex = 0;
  int? _selectedOptionIndex;
  bool _isAnswered = false;
  int _score = 0;

  void _handleOptionSelect(int index) {
    if (_isAnswered) return;
    setState(() {
      _selectedOptionIndex = index;
    });
  }

  void _checkAnswer() {
    if (_selectedOptionIndex == null) return;

    setState(() {
      _isAnswered = true;
      if (_selectedOptionIndex == widget.quiz.questions[_currentQuestionIndex].correctAnswerIndex) {
        _score++;
      }
    });
  }

  void _nextQuestion() {
    if (_currentQuestionIndex < widget.quiz.questions.length - 1) {
      setState(() {
        _currentQuestionIndex++;
        _selectedOptionIndex = null;
        _isAnswered = false;
      });
    } else {
      _showResults();
    }
  }

  void _showResults() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        title: const Text('Quiz Complete!'),
        content: Text('You scored $_score out of ${widget.quiz.questions.length}'),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(context).pop(); // Pop dialog
              Navigator.of(context).pop(); // Pop quiz screen
            },
            child: const Text('Finish'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final question = widget.quiz.questions[_currentQuestionIndex];

    return Scaffold(
      appBar: AppBar(
        title: Text('Question ${_currentQuestionIndex + 1}/${widget.quiz.questions.length}'),
        leading: IconButton(
          icon: const Icon(Icons.close),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            LinearProgressIndicator(
              value: (_currentQuestionIndex + 1) / widget.quiz.questions.length,
              backgroundColor: Colors.grey.shade200,
              color: AppColors.primarySkyBlue,
              borderRadius: BorderRadius.circular(10),
            ),
            const SizedBox(height: 40),
            Text(
              question.question,
              style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 32),
            ...List.generate(question.options.length, (index) {
              final isSelected = _selectedOptionIndex == index;
              final isCorrect = question.correctAnswerIndex == index;

              Color borderColor = Colors.grey.shade300;
              Color bgColor = Colors.white;

              if (_isAnswered) {
                if (isCorrect) {
                  borderColor = AppColors.secondaryGreen;
                  bgColor = AppColors.secondaryGreen.withOpacity(0.1);
                } else if (isSelected) {
                  borderColor = AppColors.secondaryCoralRed;
                  bgColor = AppColors.secondaryCoralRed.withOpacity(0.1);
                }
              } else if (isSelected) {
                borderColor = AppColors.primarySkyBlue;
                bgColor = AppColors.primarySkyBlue.withOpacity(0.1);
              }

              return GestureDetector(
                onTap: () => _handleOptionSelect(index),
                child: Container(
                  margin: const EdgeInsets.only(bottom: 16),
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: bgColor,
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(color: borderColor, width: 2),
                  ),
                  child: Row(
                    children: [
                      Text(
                        String.fromCharCode(65 + index),
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          color: isSelected ? AppColors.primarySkyBlue : AppColors.textGrey,
                        ),
                      ),
                      const SizedBox(width: 16),
                      Expanded(child: Text(question.options[index])),
                      if (_isAnswered && isCorrect)
                        const Icon(Icons.check_circle, color: AppColors.secondaryGreen),
                      if (_isAnswered && isSelected && !isCorrect)
                        const Icon(Icons.cancel, color: AppColors.secondaryCoralRed),
                    ],
                  ),
                ),
              );
            }),
            const Spacer(),
            if (_isAnswered)
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.blue.shade50,
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  'Explanation: ${question.explanation}',
                  style: const TextStyle(fontSize: 14, fontStyle: FontStyle.italic),
                ),
              ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: _selectedOptionIndex != null ? (_isAnswered ? _nextQuestion : _checkAnswer) : null,
              child: Text(_isAnswered ? 'Next Question' : 'Check Answer'),
            ),
          ],
        ),
      ),
    );
  }
}
