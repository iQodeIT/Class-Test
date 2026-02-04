import 'package:edu_play/models/client_model.dart';
import 'package:edu_play/services/database_helper.dart';
import 'package:sqflite/sqflite.dart';

class ClientRepository {
  final DatabaseHelper _dbHelper = DatabaseHelper();

  Future<int> insert(Client client) async {
    Database db = await _dbHelper.database;
    return await db.insert('clients', client.toMap());
  }

  Future<List<Client>> getAll() async {
    Database db = await _dbHelper.database;
    List<Map<String, dynamic>> maps = await db.query('clients');
    return maps.map((map) => Client.fromMap(map)).toList();
  }

  Future<int> update(Client client) async {
    Database db = await _dbHelper.database;
    return await db.update(
      'clients',
      client.toMap(),
      where: 'id = ?',
      whereArgs: [client.id],
    );
  }

  Future<int> delete(int id) async {
    Database db = await _dbHelper.database;
    return await db.delete(
      'clients',
      where: 'id = ?',
      whereArgs: [id],
    );
  }
}
