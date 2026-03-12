import 'dart:math';
import 'package:edu_play/models/project.dart';

class NewellAIService {
  /// Simulates transcribing audio and extracting smart words.
  /// In a real app, this would call the Newell AI Gateway API.
  Future<List<TranscriptionWord>> processMedia(String audioPath) async {
    // Simulate network delay
    await Future.delayed(const Duration(seconds: 3));

    // Sample transcript for demonstration
    final rawWords = [
      "Welcome", "to", "this", "advanced", "English", "lesson.",
      "Today", "we", "will", "elucidate", "complex", "grammatical", "structures",
      "that", "often", "perplex", "even", "the", "most", "diligent", "students.",
      "The", "quintessential", "element", "of", "fluency", "is", "consistency."
    ];

    final smartWords = {
      "elucidate": "To make something clear; to explain.",
      "perplex": "To cause someone to feel completely baffled.",
      "diligent": "Having or showing care and conscientiousness.",
      "quintessential": "Representing the most perfect or typical example.",
      "fluency": "The ability to express oneself easily and articulately."
    };

    List<TranscriptionWord> result = [];
    double currentTime = 0.0;

    for (var wordText in rawWords) {
      final cleanWord = wordText.replaceAll(RegExp(r'[^\w]'), '').toLowerCase();
      final isSmart = smartWords.containsKey(cleanWord);

      final duration = 0.3 + (Random().nextDouble() * 0.4);

      result.add(TranscriptionWord(
        text: wordText,
        startTime: Duration(milliseconds: (currentTime * 1000).toInt()),
        endTime: Duration(milliseconds: ((currentTime + duration) * 1000).toInt()),
        isSmartWord: isSmart,
        definition: isSmart ? smartWords[cleanWord] : null,
      ));

      currentTime += duration + 0.1;
    }

    return result;
  }
}
