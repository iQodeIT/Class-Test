import '../models/grade_model.dart';
import '../models/subject_model.dart';
import '../models/topic_model.dart';
import '../models/lesson_model.dart';
import '../models/quiz_model.dart';

class CMSMockService {
  static List<Grade> getGrades() {
    return [
      Grade(id: 'g1', name: 'Primary 1'),
      Grade(id: 'g2', name: 'Primary 2'),
      Grade(id: 'g3', name: 'Primary 3'),
      Grade(id: 'g4', name: 'Primary 4'),
    ];
  }

  static List<Subject> getSubjects(String gradeId) {
    return [
      Subject(id: 's1', name: 'Mathematics', gradeId: gradeId),
      Subject(id: 's2', name: 'English Studies', gradeId: gradeId),
      Subject(id: 's3', name: 'Basic Science', gradeId: gradeId),
    ];
  }

  static List<Topic> getTopics(String subjectId) {
    return [
      Topic(id: 't1', name: 'Addition and Subtraction', subjectId: subjectId),
      Topic(id: 't2', name: 'Nouns and Pronouns', subjectId: subjectId),
    ];
  }

  static Lesson getLesson(String topicId) {
    return Lesson(
      id: 'l1',
      topicId: topicId,
      title: 'Introduction to Addition',
      content: 'Addition is the process of calculating the total of two or more numbers.',
    );
  }

  static List<QuizItem> getQuizzes(String topicId) {
    return [
      QuizItem(
        id: 'q1',
        topicId: topicId,
        question: 'What is 2 + 2?',
        options: ['3', '4', '5', '6'],
        correctAnswer: '4',
        explanation: '2 plus 2 equals 4.',
      ),
    ];
  }
}
