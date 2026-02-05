import 'package:edu_play/models/user_model.dart';
import 'package:flutter/foundation.dart';
import 'package:hive_flutter/hive_flutter.dart';

class AuthService extends ChangeNotifier {
  UserModel? _currentUser;
  final Box _settingsBox = Hive.box('settings');

  UserModel? get currentUser => _currentUser;
  bool get isAuthenticated => _currentUser != null;

  AuthService() {
    _loadUser();
  }

  void _loadUser() {
    final userData = _settingsBox.get('user');
    if (userData != null) {
      _currentUser = UserModel.fromJson(Map<String, dynamic>.from(userData));
      notifyListeners();
    }
  }

  Future<void> login(UserModel user) async {
    _currentUser = user;
    await _settingsBox.put('user', user.toJson());
    notifyListeners();
  }

  Future<void> logout() async {
    _currentUser = null;
    await _settingsBox.delete('user');
    notifyListeners();
  }
}
