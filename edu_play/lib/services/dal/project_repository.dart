import 'package:edu_play/models/project_model.dart';
import 'package:edu_play/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class ProjectRepository {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<int> insert(Project project) async {
    Database db = await _dbHelper.database;
    return await db.insert('projects', project.toMap());
  }

  Future<List<Project>> getAll() async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query('projects');
    return maps.map((map) => Project.fromMap(map)).toList();
  }

  Future<Project?> getById(int id) async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query(
      'projects',
      where: 'id = ?',
      whereArgs: [id],
    );
    if (maps.isNotEmpty) {
      return Project.fromMap(maps.first);
    }
    return null;
  }

  Future<int> update(Project project) async {
    Database db = await _dbHelper.database;
    return await db.update(
      'projects',
      project.toMap(),
      where: 'id = ?',
      whereArgs: [project.id],
    );
  }

  Future<int> delete(int id) async {
    Database db = await _dbHelper.database;
    return await db.delete(
      'projects',
      where: 'id = ?',
      whereArgs: [id],
    );
  }
}
