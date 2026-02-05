class Grade {
  final String id;
  final String name; // e.g., "Primary 1"

  Grade({required this.id, required this.name});
}

class Subject {
  final String id;
  final String name; // e.g., "Mathematics"
  final String icon;

  Subject({required this.id, required this.name, required this.icon});
}

class Topic {
  final String id;
  final String subjectId;
  final String name; // e.g., "Addition"

  Topic({required this.id, required this.subjectId, required this.name});
}

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
    required this.mediaUrls,
  });
}
