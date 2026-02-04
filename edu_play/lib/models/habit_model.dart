class Habit {
  final int? id;
  final String name;
  final String frequency; // 'Daily', 'Weekly'
  final int streak;
  final DateTime? lastCompleted;

  Habit({
    this.id,
    required this.name,
    this.frequency = 'Daily',
    this.streak = 0,
    this.lastCompleted,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'frequency': frequency,
      'streak': streak,
      'lastCompleted': lastCompleted?.toIso8601String(),
    };
  }

  factory Habit.fromMap(Map<String, dynamic> map) {
    return Habit(
      id: map['id'],
      name: map['name'],
      frequency: map['frequency'] ?? 'Daily',
      streak: map['streak'] ?? 0,
      lastCompleted: map['lastCompleted'] != null
          ? DateTime.parse(map['lastCompleted'])
          : null,
    );
  }
}
