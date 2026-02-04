class Resource {
  final int? id;
  final String title;
  final String content;
  final String type; // 'Note', 'Link', 'Document'
  final int? projectId;

  Resource({
    this.id,
    required this.title,
    this.content = '',
    this.type = 'Note',
    this.projectId,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'content': content,
      'type': type,
      'projectId': projectId,
    };
  }

  factory Resource.fromMap(Map<String, dynamic> map) {
    return Resource(
      id: map['id'],
      title: map['title'],
      content: map['content'] ?? '',
      type: map['type'] ?? 'Note',
      projectId: map['projectId'],
    );
  }
}
