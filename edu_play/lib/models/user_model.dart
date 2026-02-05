enum UserRole {
  student,
  parent,
  teacher,
  admin,
}

class UserModel {
  final String id;
  final String email;
  final String name;
  final UserRole role;
  final String? schoolCode;

  // Student specific fields
  final int? xp;
  final int? streak;
  final String? grade; // e.g., "Primary 1"

  UserModel({
    required this.id,
    required this.email,
    required this.name,
    required this.role,
    this.schoolCode,
    this.xp,
    this.streak,
    this.grade,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      id: json['id'],
      email: json['email'],
      name: json['name'],
      role: UserRole.values.firstWhere((e) => e.toString().split('.').last == json['role']),
      schoolCode: json['schoolCode'],
      xp: json['xp'],
      streak: json['streak'],
      grade: json['grade'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'name': name,
      'role': role.toString().split('.').last,
      'schoolCode': schoolCode,
      'xp': xp,
      'streak': streak,
      'grade': grade,
    };
  }
}
