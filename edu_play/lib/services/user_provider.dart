import 'package:flutter/material.dart';
import '../models/user_model.dart';

class UserProvider with ChangeNotifier {
  User? _user;

  User? get user => _user;

  void setUser(User user) {
    _user = user;
    notifyListeners();
  }

  void setRole(String role) {
    if (_user != null) {
      _user = User(id: _user!.id, email: _user!.email, role: role);
    } else {
      _user = User(id: 'temp_id', email: 'guest@eduplay.com', role: role);
    }
    notifyListeners();
  }

  void logout() {
    _user = null;
    notifyListeners();
  }
}
