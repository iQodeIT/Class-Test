import 'package:flutter/material.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:edu_play/models/project.dart';

class ProjectProvider extends ChangeNotifier {
  static const String boxName = 'projects_box';
  List<Project> _projects = [];

  List<Project> get projects => _projects;

  Future<void> init() async {
    // Note: Type adapters must be registered before opening the box.
    // We'll handle registration in main.dart or a global init.
    final box = await Hive.openBox<Project>(boxName);
    _projects = box.values.toList()..sort((a, b) => b.createdAt.compareTo(a.createdAt));
    notifyListeners();
  }

  Future<void> addProject(Project project) async {
    final box = Hive.box<Project>(boxName);
    await box.put(project.id, project);
    _projects.insert(0, project);
    notifyListeners();
  }

  Future<void> updateProject(Project project) async {
    await project.save();
    notifyListeners();
  }

  Future<void> deleteProject(String id) async {
    final box = Hive.box<Project>(boxName);
    await box.delete(id);
    _projects.removeWhere((p) => p.id == id);
    notifyListeners();
  }
}
