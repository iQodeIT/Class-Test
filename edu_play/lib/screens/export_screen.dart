import 'package:edu_play/models/project.dart';
import 'package:edu_play/utils/app_theme.dart';
import 'package:flutter/material.dart';

class ExportScreen extends StatefulWidget {
  final Project project;

  const ExportScreen({super.key, required this.project});

  @override
  State<ExportScreen> createState() => _ExportScreenState();
}

class _ExportScreenState extends State<ExportScreen> {
  double _progress = 0.0;
  bool _isExporting = false;
  bool _isDone = false;
  String _selectedRes = "1080p";

  void _startExport() async {
    setState(() => _isExporting = true);
    for (int i = 0; i <= 100; i += 5) {
      await Future.delayed(const Duration(milliseconds: 200));
      if (mounted) setState(() => _progress = i / 100);
    }
    if (mounted) {
      setState(() {
        _isExporting = false;
        _isDone = true;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('EXPORT PROJECT')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            if (!_isExporting && !_isDone) ...[
              _buildSettings(),
            ] else if (_isExporting) ...[
              _buildExporting(),
            ] else ...[
              _buildRecap(),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildSettings() {
    return Column(
      children: [
        const Icon(Icons.movie_filter, size: 80, color: AppTheme.linguaGold),
        const SizedBox(height: 24),
        Text("Finalize your Video", style: Theme.of(context).textTheme.displaySmall),
        const SizedBox(height: 32),
        _buildResolutionCard("720p", "Balanced quality & size"),
        const SizedBox(height: 16),
        _buildResolutionCard("1080p", "High definition (Pro)"),
        const SizedBox(height: 48),
        SizedBox(
          width: double.infinity,
          height: 56,
          child: ElevatedButton(
            onPressed: _startExport,
            child: const Text('RENDER VIDEO'),
          ),
        ),
      ],
    );
  }

  Widget _buildResolutionCard(String res, String desc) {
    final isSelected = _selectedRes == res;
    return GestureDetector(
      onTap: () => setState(() => _selectedRes = res),
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: isSelected ? AppTheme.linguaGold.withOpacity(0.1) : AppTheme.surfaceGray,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: isSelected ? AppTheme.linguaGold : Colors.white12),
        ),
        child: Row(
          children: [
            Icon(Icons.hd, color: isSelected ? AppTheme.linguaGold : Colors.white54),
            const SizedBox(width: 16),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(res, style: TextStyle(fontWeight: FontWeight.bold, color: isSelected ? AppTheme.linguaGold : Colors.white)),
                Text(desc, style: const TextStyle(fontSize: 12, color: Colors.white54)),
              ],
            ),
            const Spacer(),
            if (isSelected) const Icon(Icons.check_circle, color: AppTheme.linguaGold),
          ],
        ),
      ),
    );
  }

  Widget _buildExporting() {
    return Column(
      children: [
        const SizedBox(height: 60),
        const CircularProgressIndicator(valueColor: AlwaysStoppedAnimation(AppTheme.linguaGold), strokeWidth: 8),
        const SizedBox(height: 40),
        Text("${(_progress * 100).toInt()}%", style: const TextStyle(fontSize: 48, fontWeight: FontWeight.bold, color: AppTheme.linguaGold)),
        const SizedBox(height: 16),
        const Text("Burning in captions & smart highlights...", style: TextStyle(color: AppTheme.slateGray)),
        const SizedBox(height: 40),
        LinearProgressIndicator(value: _progress, backgroundColor: AppTheme.surfaceGray, color: AppTheme.linguaGold),
      ],
    );
  }

  Widget _buildRecap() {
    final smartWords = widget.project.transcript?.where((w) => w.isSmartWord).toList() ?? [];
    return Column(
      children: [
        const Icon(Icons.celebration, size: 60, color: AppTheme.linguaGold),
        const SizedBox(height: 16),
        const Text("Export Complete!", style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
        const SizedBox(height: 32),

        // Vocabulary Recap Card
        Container(
          padding: const EdgeInsets.all(24),
          decoration: BoxDecoration(
            color: AppTheme.surfaceGray,
            borderRadius: BorderRadius.circular(24),
            border: Border.all(color: AppTheme.linguaGold.withOpacity(0.3)),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text("Words from this episode:", style: TextStyle(fontWeight: FontWeight.bold, color: AppTheme.linguaGold)),
              const SizedBox(height: 16),
              ...smartWords.asMap().entries.map((entry) {
                return TweenAnimationBuilder<double>(
                  tween: Tween(begin: 0.0, end: 1.0),
                  duration: Duration(milliseconds: 500 + (entry.key * 200)),
                  builder: (context, value, child) => Opacity(opacity: value, child: Transform.translate(offset: Offset(20 * (1 - value), 0), child: child)),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 4.0),
                    child: Row(
                      children: [
                        const Icon(Icons.star, size: 14, color: AppTheme.linguaGold),
                        const SizedBox(width: 8),
                        Text(entry.value.text, style: const TextStyle(fontWeight: FontWeight.w600)),
                      ],
                    ),
                  ),
                );
              }),
            ],
          ),
        ),

        const SizedBox(height: 48),
        Row(
          children: [
            Expanded(
              child: OutlinedButton.icon(
                onPressed: () {},
                icon: const Icon(Icons.share),
                label: const Text("SHARE"),
                style: OutlinedButton.styleFrom(side: const BorderSide(color: AppTheme.linguaGold), foregroundColor: AppTheme.linguaGold),
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: ElevatedButton.icon(
                onPressed: () => Navigator.popUntil(context, (route) => route.isFirst),
                icon: const Icon(Icons.home),
                label: const Text("DASHBOARD"),
              ),
            ),
          ],
        ),
      ],
    );
  }
}
