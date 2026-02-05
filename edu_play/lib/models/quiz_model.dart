// Represents a single quiz question.
class QuizItem {
  final String id;
  final String topicId;
  final String question;
  final List<String> options;
  final String correctAnswer;
  final String explanation;

  QuizItem({
    required this.id,
    required this.topicId,
    required this.question,
    required this.options,
    required this.correctAnswer,
    required this.explanation,
  });
}