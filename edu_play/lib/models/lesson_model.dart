// Represents a single lesson in a topic.
class Lesson {
  final String id;
  final String topicId;
  final String title;
  final String content;
  final List<String> mediaUrls;

  Lesson({
    required this.id,
    required this.topicId,
    required this.title,
    required this.content,
    this.mediaUrls = const [],
  });
}