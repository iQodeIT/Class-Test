class Project {
  final int? id;
  final String name;
  final String description;
  final double progress;
  final String status;

  Project({
    this.id,
    required this.name,
    this.description = '',
    this.progress = 0.0,
    this.status = 'Active',
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'progress': progress,
      'status': status,
    };
  }

  factory Project.fromMap(Map<String, dynamic> map) {
    return Project(
      id: map['id'],
      name: map['name'],
      description: map['description'] ?? '',
      progress: (map['progress'] ?? 0.0).toDouble(),
      status: map['status'] ?? 'Active',
    );
  }
}
