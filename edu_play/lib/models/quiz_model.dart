// Represents a single quiz question.
class QuizItem {
  final String id;
  final String question;
  final List<String> options;
  final String correctAnswer;

  QuizItem({
    required this.id,
    required this.question,
    required this.options,
    required this.correctAnswer,
  });
}