// Represents a user in the EduPlay app.
// This will be expanded to include properties for each user role.
class User {
  final String id;
  final String email;
  final String role; // "Student", "Parent", "Teacher", "Admin"

  User({
    required this.id,
    required this.email,
    required this.role,
  });
}