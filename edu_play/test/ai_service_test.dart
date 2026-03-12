import 'package:flutter_test/flutter_test.dart';
import 'package:edu_play/services/newell_ai_service.dart';

void main() {
  test('NewellAIService simulation returns smart words', () async {
    final service = NewellAIService();
    final result = await service.processMedia('dummy_path');

    expect(result.isNotEmpty, true);
    final smartWords = result.where((w) => w.isSmartWord).toList();
    expect(smartWords.isNotEmpty, true);

    // Check if a known smart word is present
    final hasElucidate = smartWords.any((w) => w.text.toLowerCase().contains('elucidate'));
    expect(hasElucidate, true);
    expect(smartWords.first.definition, isNotNull);
  });
}
