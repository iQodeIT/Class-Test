class Client {
  final int? id;
  final String name;
  final String email;
  final String phone;
  final String company;

  Client({
    this.id,
    required this.name,
    this.email = '',
    this.phone = '',
    this.company = '',
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'email': email,
      'phone': phone,
      'company': company,
    };
  }

  factory Client.fromMap(Map<String, dynamic> map) {
    return Client(
      id: map['id'],
      name: map['name'],
      email: map['email'] ?? '',
      phone: map['phone'] ?? '',
      company: map['company'] ?? '',
    );
  }
}
