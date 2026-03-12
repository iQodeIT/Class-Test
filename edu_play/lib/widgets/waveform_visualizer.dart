import 'package:edu_play/utils/app_theme.dart';
import 'package:flutter/material.dart';
import 'dart:math';

class WaveformVisualizer extends StatefulWidget {
  final bool isPlaying;
  const WaveformVisualizer({super.key, required this.isPlaying});

  @override
  State<WaveformVisualizer> createState() => _WaveformVisualizerState();
}

class _WaveformVisualizerState extends State<WaveformVisualizer> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  final List<double> _heights = List.generate(42, (index) => Random().nextDouble());

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(vsync: this, duration: const Duration(milliseconds: 500))..repeat(reverse: true);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (!widget.isPlaying) {
      return Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: _heights.map((h) => Container(
          width: 2,
          height: 10 + (h * 20),
          decoration: BoxDecoration(color: AppTheme.linguaGold.withOpacity(0.5), borderRadius: BorderRadius.circular(1)),
        )).toList(),
      );
    }

    return AnimatedBuilder(
      animation: _controller,
      builder: (context, child) {
        return Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: _heights.map((h) {
            final dynamicHeight = 10 + (h * 30 * _controller.value);
            return Container(
              width: 2,
              height: dynamicHeight,
              decoration: BoxDecoration(
                color: AppTheme.linguaGold,
                borderRadius: BorderRadius.circular(1),
                boxShadow: [
                  BoxShadow(color: AppTheme.linguaGold.withOpacity(0.3), blurRadius: 4, spreadRadius: 1),
                ],
              ),
            );
          }).toList(),
        );
      },
    );
  }
}
