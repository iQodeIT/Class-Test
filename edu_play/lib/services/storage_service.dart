import 'package:hive_flutter/hive_flutter.dart';

class StorageService {
  static final Box _settingsBox = Hive.box('settings');
  static final Box _lessonsBox = Hive.box('lessons');

  static Future<void> saveUserRole(String role) async {
    await _settingsBox.put('user_role', role);
  }

  static String? getUserRole() {
    return _settingsBox.get('user_role');
  }

  static Future<void> clearAll() async {
    await _settingsBox.clear();
    await _lessonsBox.clear();
  }

  // Future methods to cache/retrieve lessons could go here
}
