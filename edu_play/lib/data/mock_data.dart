import 'package:edu_play/models/lesson_model.dart';

final List<Grade> mockGrades = [
  Grade(id: '1', name: 'Primary 1'),
  Grade(id: '2', name: 'Primary 2'),
  Grade(id: '3', name: 'Primary 3'),
];

final List<Subject> mockSubjects = [
  Subject(id: 'math', name: 'Mathematics', icon: 'numbers'),
  Subject(id: 'english', name: 'English', icon: 'book'),
  Subject(id: 'science', name: 'Basic Science', icon: 'science'),
];

final List<Topic> mockTopics = [
  Topic(id: 't1', subjectId: 'math', name: 'Addition'),
  Topic(id: 't2', subjectId: 'math', name: 'Subtraction'),
  Topic(id: 't3', subjectId: 'english', name: 'Nouns'),
];
