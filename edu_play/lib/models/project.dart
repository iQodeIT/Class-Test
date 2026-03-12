import 'package:hive/hive.dart';

part 'project.g.dart';

@HiveType(typeId: 0)
enum AspectRatioType {
  @HiveField(0)
  tiktok, // 9:16
  @HiveField(1)
  youtube, // 16:9
  @HiveField(2)
  instagram // 1:1
}

@HiveType(typeId: 1)
class Project extends HiveObject {
  @HiveField(0)
  String id;

  @HiveField(1)
  String name;

  @HiveField(2)
  String imagePath;

  @HiveField(3)
  String audioPath;

  @HiveField(4)
  AspectRatioType aspectRatio;

  @HiveField(5)
  DateTime createdAt;

  @HiveField(6)
  List<TranscriptionWord>? transcript;

  @HiveField(7)
  Map<String, String>? settings; // for font size, position etc.

  Project({
    required this.id,
    required this.name,
    required this.imagePath,
    required this.audioPath,
    required this.aspectRatio,
    required this.createdAt,
    this.transcript,
    this.settings,
  });
}

@HiveType(typeId: 2)
class TranscriptionWord {
  @HiveField(0)
  String text;

  @HiveField(1)
  Duration startTime;

  @HiveField(2)
  Duration endTime;

  @HiveField(3)
  bool isSmartWord;

  @HiveField(4)
  String? definition;

  TranscriptionWord({
    required this.text,
    required this.startTime,
    required this.endTime,
    this.isSmartWord = false,
    this.definition,
  });
}
