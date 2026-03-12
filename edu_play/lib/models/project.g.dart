// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'project.dart';

// **************************************************************************
// TypeAdapterGenerator
// **************************************************************************

class ProjectAdapter extends TypeAdapter<Project> {
  @override
  final int typeId = 1;

  @override
  Project read(BinaryReader reader) {
    final numOfFields = reader.readByte();
    final fields = <int, dynamic>{
      for (int i = 0; i < numOfFields; i++) reader.readByte(): reader.read(),
    };
    return Project(
      id: fields[0] as String,
      name: fields[1] as String,
      imagePath: fields[2] as String,
      audioPath: fields[3] as String,
      aspectRatio: fields[4] as AspectRatioType,
      createdAt: fields[5] as DateTime,
      transcript: (fields[6] as List?)?.cast<TranscriptionWord>(),
      settings: (fields[7] as Map?)?.cast<String, String>(),
    );
  }

  @override
  void write(BinaryWriter writer, Project obj) {
    writer
      ..writeByte(8)
      ..writeByte(0)
      ..write(obj.id)
      ..writeByte(1)
      ..write(obj.name)
      ..writeByte(2)
      ..write(obj.imagePath)
      ..writeByte(3)
      ..write(obj.audioPath)
      ..writeByte(4)
      ..write(obj.aspectRatio)
      ..writeByte(5)
      ..write(obj.createdAt)
      ..writeByte(6)
      ..write(obj.transcript)
      ..writeByte(7)
      ..write(obj.settings);
  }

  @override
  int get hashCode => typeId.hashCode;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is ProjectAdapter &&
          runtimeType == other.runtimeType &&
          typeId == other.typeId;
}

class TranscriptionWordAdapter extends TypeAdapter<TranscriptionWord> {
  @override
  final int typeId = 2;

  @override
  TranscriptionWord read(BinaryReader reader) {
    final numOfFields = reader.readByte();
    final fields = <int, dynamic>{
      for (int i = 0; i < numOfFields; i++) reader.readByte(): reader.read(),
    };
    return TranscriptionWord(
      text: fields[0] as String,
      startTime: fields[1] as Duration,
      endTime: fields[2] as Duration,
      isSmartWord: fields[3] as bool,
      definition: fields[4] as String?,
    );
  }

  @override
  void write(BinaryWriter writer, TranscriptionWord obj) {
    writer
      ..writeByte(5)
      ..writeByte(0)
      ..write(obj.text)
      ..writeByte(1)
      ..write(obj.startTime)
      ..writeByte(2)
      ..write(obj.endTime)
      ..writeByte(3)
      ..write(obj.isSmartWord)
      ..writeByte(4)
      ..write(obj.definition);
  }

  @override
  int get hashCode => typeId.hashCode;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is TranscriptionWordAdapter &&
          runtimeType == other.runtimeType &&
          typeId == other.typeId;
}

class AspectRatioTypeAdapter extends TypeAdapter<AspectRatioType> {
  @override
  final int typeId = 0;

  @override
  AspectRatioType read(BinaryReader reader) {
    switch (reader.readByte()) {
      case 0:
        return AspectRatioType.tiktok;
      case 1:
        return AspectRatioType.youtube;
      case 2:
        return AspectRatioType.instagram;
      default:
        return AspectRatioType.tiktok;
    }
  }

  @override
  void write(BinaryWriter writer, AspectRatioType obj) {
    switch (obj) {
      case AspectRatioType.tiktok:
        writer.writeByte(0);
        break;
      case AspectRatioType.youtube:
        writer.writeByte(1);
        break;
      case AspectRatioType.instagram:
        writer.writeByte(2);
        break;
    }
  }

  @override
  int get hashCode => typeId.hashCode;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is AspectRatioTypeAdapter &&
          runtimeType == other.runtimeType &&
          typeId == other.typeId;
}
