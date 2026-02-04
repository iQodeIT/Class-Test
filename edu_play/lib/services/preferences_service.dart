import 'package:shared_preferences/shared_preferences.dart';

class PreferencesService {
  static const String _keyEnergyLevel = 'energy_level';
  static const String _keyThemeMode = 'theme_mode';
  static const String _keyOnboardingComplete = 'onboarding_complete';

  Future<void> setEnergyLevel(String level) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyEnergyLevel, level);
  }

  Future<String> getEnergyLevel() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_keyEnergyLevel) ?? 'High';
  }

  Future<void> setThemeMode(String mode) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyThemeMode, mode);
  }

  Future<String> getThemeMode() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_keyThemeMode) ?? 'light';
  }

  Future<void> setOnboardingComplete(bool complete) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool(_keyOnboardingComplete, complete);
  }

  Future<bool> isOnboardingComplete() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool(_keyOnboardingComplete) ?? false;
  }
}
