// Represents a reward, such as a badge or avatar item.
class Reward {
  final String id;
  final String name;
  final String description;
  final String imageUrl;

  Reward({
    required this.id,
    required this.name,
    required this.description,
    required this.imageUrl,
  });
}