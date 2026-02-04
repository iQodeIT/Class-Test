import 'dart:convert';
import 'package:edu_play/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class ExportService {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<String> exportToJson() async {
    Database db = await _dbHelper.database;

    Map<String, List<Map<String, dynamic>>> allData = {};

    List<String> tables = ['projects', 'clients', 'tasks', 'habits', 'resources'];

    for (String table in tables) {
      allData[table] = await db.query(table);
    }

    return jsonEncode(allData);
  }

  // Import would involve parsing JSON and inserting into DB
  Future<void> importFromJson(String jsonString) async {
    Database db = await _dbHelper.database;
    Map<String, dynamic> data = jsonDecode(jsonString);

    await db.transaction((txn) async {
      for (String table in data.keys) {
        List<dynamic> rows = data[table];
        for (var row in rows) {
          await txn.insert(
            table,
            row as Map<String, dynamic>,
            conflictAlgorithm: ConflictAlgorithm.replace,
          );
        }
      }
    });
  }
}
