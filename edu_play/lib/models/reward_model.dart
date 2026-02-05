class Badge {
  final String id;
  final String name;
  final String description;
  final String iconUrl;

  Badge({
    required this.id,
    required this.name,
    required this.description,
    required this.iconUrl,
  });
}

class Reward {
  final String id;
  final String name;
  final int costXP;
  final String imageUrl;

  Reward({
    required this.id,
    required this.name,
    required this.costXP,
    required this.imageUrl,
  });
}
