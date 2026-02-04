import 'package:edu_play/models/task_model.dart';
import 'package:edu_play/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class TaskRepository {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<int> insert(Task task) async {
    Database db = await _dbHelper.database;
    return await db.insert('tasks', task.toMap());
  }

  Future<List<Task>> getAll() async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query('tasks');
    return maps.map((map) => Task.fromMap(map)).toList();
  }

  Future<List<Task>> getByCategory(String category) async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query(
      'tasks',
      where: 'category = ?',
      whereArgs: [category],
    );
    return maps.map((map) => Task.fromMap(map)).toList();
  }

  Future<List<Task>> getByProject(int projectId) async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query(
      'tasks',
      where: 'projectId = ?',
      whereArgs: [projectId],
    );
    return maps.map((map) => Task.fromMap(map)).toList();
  }

  Future<int> update(Task task) async {
    Database db = await _dbHelper.database;
    return await db.update(
      'tasks',
      task.toMap(),
      where: 'id = ?',
      whereArgs: [task.id],
    );
  }

  Future<int> delete(int id) async {
    Database db = await _dbHelper.database;
    return await db.delete(
      'tasks',
      where: 'id = ?',
      whereArgs: [id],
    );
  }
}
