import 'package:flutter/material.dart';
// import 'package:paystack_flutter_sdk/paystack_flutter_sdk.dart';

class PaymentService {
  static Future<void> processPayment(BuildContext context, double amount) async {
    // Placeholder for Paystack payment processing
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Processing payment of ₦$amount via Paystack...')),
    );
    await Future.delayed(const Duration(seconds: 2));
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Payment Successful!')),
    );
  }
}
