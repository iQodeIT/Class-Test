# EduPlay CMS Schema (Airtable/Supabase)

This document outlines the data schema for the EduPlay application's content management system (CMS).

---

## 1. `Grades` Table

Stores the different educational grade levels.

| Column Name | Data Type | Description | Example |
| :---------- | :-------- | :---------- | :------ |
| `id` | `UUID` (Primary Key) | Unique identifier for the grade. | `uuid_generate_v4()` |
| `name` | `Text` | The name of the grade. | "Primary 1" |
| `description` | `Text` | A brief description of the grade level. | "For students aged 5-6." |
| `order` | `Integer` | The display order for the grades. | `1` |

---

## 2. `Subjects` Table

Stores the subjects for each grade.

| Column Name | Data Type | Description | Example |
| :---------- | :-------- | :---------- | :------ |
| `id` | `UUID` (Primary Key) | Unique identifier for the subject. | `uuid_generate_v4()` |
| `grade_id` | `UUID` (Foreign Key -> `Grades.id`) | The grade this subject belongs to. | `(ID of Primary 1)` |
| `name` | `Text` | The name of the subject. | "Mathematics" |
| `icon_url` | `URL` | URL to an icon representing the subject. | `https://cdn.eduplay.app/icons/math.png` |

---

## 3. `Topics` Table

Stores the topics within each subject.

| Column Name | Data Type | Description | Example |
| :---------- | :-------- | :---------- | :------ |
| `id` | `UUID` (Primary Key) | Unique identifier for the topic. | `uuid_generate_v4()` |
| `subject_id` | `UUID` (Foreign Key -> `Subjects.id`) | The subject this topic belongs to. | `(ID of Mathematics)` |
| `name` | `Text` | The name of the topic. | "Addition and Subtraction" |
| `order` | `Integer` | The display order for topics within a subject. | `1` |

---

## 4. `Lessons` Table (renamed from `LessonAssets`)

Stores the actual lesson content for each topic.

| Column Name | Data Type | Description | Example |
| :---------- | :-------- | :---------- | :------ |
| `id` | `UUID` (Primary Key) | Unique identifier for the lesson. | `uuid_generate_v4()` |
| `topic_id` | `UUID` (Foreign Key -> `Topics.id`) | The topic this lesson belongs to. | `(ID of Addition)` |
| `title` | `Text` | The title of the lesson. | "Adding Numbers up to 10" |
| `content` | `Rich Text / Markdown` | The main body of the lesson text. | "Adding is when you combine two or more numbers..." |
| `video_url` | `URL` (Optional) | URL to a supplementary video. | `https://youtube.com/watch?v=...` |
| `audio_narration_url` | `URL` (Optional) | URL to a voiceover for the lesson. | `https://cdn.eduplay.app/audio/lesson1.mp3` |
| `order` | `Integer` | The display order for lessons within a topic. | `1` |

---

## 5. `QuizItems` Table

Stores questions and answers for quizzes, linked to a lesson.

| Column Name | Data Type | Description | Example |
| :---------- | :-------- | :---------- | :------ |
| `id` | `UUID` (Primary Key) | Unique identifier for the quiz item. | `uuid_generate_v4()` |
| `lesson_id` | `UUID` (Foreign Key -> `Lessons.id`) | The lesson this quiz question is for. | `(ID of Lesson 1)` |
| `question_text` | `Text` | The text of the question. | "What is 2 + 3?" |
| `question_type` | `Enum` (`MCQ`, `FillInBlank`) | The type of question. | `MCQ` |
| `options` | `JSON / Array` | An array of possible answers for MCQ. | `["4", "5", "6"]` |
| `correct_answer` | `Text` | The correct answer. | `"5"` |
| `explanation` | `Text` (Optional) | An explanation for the correct answer. | "When you add 2 and 3, you get 5." |

---

## 6. `Rewards` Table

Stores the badges and avatar items that can be unlocked.

| Column Name | Data Type | Description | Example |
| :---------- | :-------- | :---------- | :------ |
| `id` | `UUID` (Primary Key) | Unique identifier for the reward. | `uuid_generate_v4()` |
| `name` | `Text` | The name of the reward. | "Math Whiz Badge" |
| `description` | `Text` | How to earn the reward. | "Complete all Addition lessons." |
| `reward_type` | `Enum` (`Badge`, `AvatarItem`) | The type of reward. | `Badge` |
| `image_url` | `URL` | URL to the badge or item image. | `https://cdn.eduplay.app/rewards/math_whiz.png` |
| `xp_cost` | `Integer` (Optional) | The XP cost to unlock (if applicable). | `500` |
| `unlock_milestone` | `Text` (Optional) | The specific milestone required to unlock it. | `TOPIC_COMPLETE: (ID of Addition)` |