import * as SQLite from 'expo-sqlite';

const DB_NAME = 'soulstice.db';

export const initDatabase = async () => {
  const db = await SQLite.openDatabaseAsync(DB_NAME);

  await db.execAsync(`
    PRAGMA journal_mode = WAL;
    CREATE TABLE IF NOT EXISTS tasks (
      id TEXT PRIMARY KEY NOT NULL,
      title TEXT NOT NULL,
      description TEXT,
      status TEXT DEFAULT 'todo',
      priority TEXT DEFAULT 'medium',
      energy TEXT DEFAULT 'high',
      projectId TEXT,
      dueDate TEXT,
      createdAt TEXT NOT NULL
    );
    CREATE TABLE IF NOT EXISTS projects (
      id TEXT PRIMARY KEY NOT NULL,
      name TEXT NOT NULL,
      description TEXT,
      status TEXT DEFAULT 'active',
      createdAt TEXT NOT NULL
    );
    CREATE TABLE IF NOT EXISTS habits (
      id TEXT PRIMARY KEY NOT NULL,
      title TEXT NOT NULL,
      frequency TEXT DEFAULT 'daily',
      streak INTEGER DEFAULT 0,
      lastCompleted TEXT,
      createdAt TEXT NOT NULL
    );
  `);
};

export const getTasks = async () => {
  const db = await SQLite.openDatabaseAsync(DB_NAME);
  return await db.getAllAsync('SELECT * FROM tasks ORDER BY createdAt DESC');
};

export const addTask = async (task: any) => {
  const db = await SQLite.openDatabaseAsync(DB_NAME);
  await db.runAsync(
    'INSERT INTO tasks (id, title, description, status, priority, energy, projectId, dueDate, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)',
    [task.id, task.title, task.description || null, task.status, task.priority, task.energy, task.projectId || null, task.dueDate || null, task.createdAt]
  );
};
