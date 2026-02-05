import 'package:flutter_test/flutter_test.dart';
import 'package:edu_play/main.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:flutter/material.dart';
import 'dart:io';

void main() {
  // Setup Hive for testing
  setUp(() async {
    final tempDir = await Directory.systemTemp.createTemp();
    Hive.init(tempDir.path);
    await Hive.openBox('settings');
  });

  tearDown(() async {
    await Hive.close();
  });

  testWidgets('App starts with Onboarding Screen', (WidgetTester tester) async {
    await tester.pumpWidget(const MyApp());
    await tester.pumpAndSettle();

    // Verify that we are on the onboarding screen
    expect(find.text('Welcome to EduPlay!'), findsOneWidget);
    expect(find.text('Student'), findsOneWidget);
  });
}
