import 'package:edu_play/models/habit_model.dart';
import 'package:edu_play/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class HabitRepository {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<int> insert(Habit habit) async {
    Database db = await _dbHelper.database;
    return await db.insert('habits', habit.toMap());
  }

  Future<List<Habit>> getAll() async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query('habits');
    return maps.map((map) => Habit.fromMap(map)).toList();
  }

  Future<int> update(Habit habit) async {
    Database db = await _dbHelper.database;
    return await db.update(
      'habits',
      habit.toMap(),
      where: 'id = ?',
      whereArgs: [habit.id],
    );
  }

  Future<int> delete(int id) async {
    Database db = await _dbHelper.database;
    return await db.delete(
      'habits',
      where: 'id = ?',
      whereArgs: [id],
    );
  }
}
