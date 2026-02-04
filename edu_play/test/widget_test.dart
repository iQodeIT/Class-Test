import 'package:edu_play/main.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Soulstice welcome message test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const SoulsticeApp());

    // Verify that our welcome message is displayed.
    expect(find.text('Welcome to Soulstice'), findsOneWidget);
    expect(find.text('Core Data Architecture Foundation is being built...'), findsOneWidget);
  });
}
