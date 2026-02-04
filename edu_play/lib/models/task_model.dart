class Task {
  final int? id;
  final String title;
  final String description;
  final bool isCompleted;
  final DateTime? dueDate;
  final String energyLevel; // 'High', 'Low'
  final String category; // 'Today', 'Active', 'Incubator', 'Leads'
  final int? projectId;
  final int? clientId;
  final DateTime? updatedAt;

  Task({
    this.id,
    required this.title,
    this.description = '',
    this.isCompleted = false,
    this.dueDate,
    this.energyLevel = 'High',
    this.category = 'Active',
    this.projectId,
    this.clientId,
    this.updatedAt,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'isCompleted': isCompleted ? 1 : 0,
      'dueDate': dueDate?.toIso8601String(),
      'energyLevel': energyLevel,
      'category': category,
      'projectId': projectId,
      'clientId': clientId,
      'updatedAt': updatedAt?.toIso8601String() ?? DateTime.now().toIso8601String(),
    };
  }

  factory Task.fromMap(Map<String, dynamic> map) {
    return Task(
      id: map['id'],
      title: map['title'],
      description: map['description'] ?? '',
      isCompleted: map['isCompleted'] == 1,
      dueDate: map['dueDate'] != null ? DateTime.parse(map['dueDate']) : null,
      energyLevel: map['energyLevel'] ?? 'High',
      category: map['category'] ?? 'Active',
      projectId: map['projectId'],
      clientId: map['clientId'],
      updatedAt: map['updatedAt'] != null ? DateTime.parse(map['updatedAt']) : null,
    );
  }
}
