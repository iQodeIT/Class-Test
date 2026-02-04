import 'package:edu_play/models/resource_model.dart';
import 'package:edu_play/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class ResourceRepository {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<int> insert(Resource resource) async {
    Database db = await _dbHelper.database;
    return await db.insert('resources', resource.toMap());
  }

  Future<List<Resource>> getByProject(int projectId) async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query(
      'resources',
      where: 'projectId = ?',
      whereArgs: [projectId],
    );
    return maps.map((map) => Resource.fromMap(map)).toList();
  }

  Future<int> update(Resource resource) async {
    Database db = await _dbHelper.database;
    return await db.update(
      'resources',
      resource.toMap(),
      where: 'id = ?',
      whereArgs: [resource.id],
    );
  }

  Future<int> delete(int id) async {
    Database db = await _dbHelper.database;
    return await db.delete(
      'resources',
      where: 'id = ?',
      whereArgs: [id],
    );
  }
}
