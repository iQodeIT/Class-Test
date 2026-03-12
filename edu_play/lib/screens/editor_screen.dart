import 'dart:io';
import 'package:edu_play/models/project.dart';
import 'package:edu_play/utils/app_theme.dart';
import 'package:edu_play/widgets/definition_card.dart';
import 'package:edu_play/widgets/waveform_visualizer.dart';
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';

class EditorScreen extends StatefulWidget {
  final Project project;

  const EditorScreen({super.key, required this.project});

  @override
  State<EditorScreen> createState() => _EditorScreenState();
}

class _EditorScreenState extends State<EditorScreen> with SingleTickerProviderStateMixin {
  late VideoPlayerController _controller;
  bool _isPlaying = false;
  Duration _currentPosition = Duration.zero;
  TranscriptionWord? _activeSmartWord;
  int _activeTabIndex = 0;

  // Styling state
  double _fontSize = 24.0;
  double _bgOpacity = 0.4;

  @override
  void initState() {
    super.initState();
    // In this app, we "play" an image + audio as a video.
    // For the preview, we can use the audio controller to drive the UI.
    _controller = VideoPlayerController.file(File(widget.project.audioPath))
      ..initialize().then((_) {
        setState(() {});
      });

    _controller.addListener(() {
      if (mounted) {
        setState(() {
          _currentPosition = _controller.value.position;
          _updateActiveSmartWord();
        });
      }
    });
  }

  void _updateActiveSmartWord() {
    final words = widget.project.transcript ?? [];
    TranscriptionWord? found;
    for (var w in words) {
      if (w.isSmartWord && _currentPosition >= w.startTime && _currentPosition <= w.endTime) {
        found = w;
        break;
      }
    }
    if (found != _activeSmartWord) {
      setState(() => _activeSmartWord = found);
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    double aspectRatio = 9 / 16;
    if (widget.project.aspectRatio == AspectRatioType.youtube) aspectRatio = 16 / 9;
    if (widget.project.aspectRatio == AspectRatioType.instagram) aspectRatio = 1 / 1;

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.project.name.toUpperCase(), style: const TextStyle(fontSize: 16)),
        actions: [
          TextButton(
            onPressed: () => Navigator.pushNamed(context, '/export', arguments: widget.project),
            child: const Text('EXPORT', style: TextStyle(color: AppTheme.linguaGold)),
          ),
        ],
      ),
      body: Column(
        children: [
          // Video Preview Area
          Expanded(
            flex: 3,
            child: Center(
              child: AspectRatio(
                aspectRatio: aspectRatio,
                child: Container(
                  margin: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: Colors.black,
                    borderRadius: BorderRadius.circular(12),
                    boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.5), blurRadius: 20)],
                  ),
                  clipBehavior: Clip.antiAlias,
                  child: Stack(
                    alignment: Alignment.center,
                    children: [
                      // Background Image
                      Image.file(File(widget.project.imagePath), fit: BoxFit.cover, width: double.infinity, height: double.infinity),

                      // Dark overlay
                      Container(color: Colors.black.withOpacity(0.3)),

                      // Captions Layer
                      Positioned(
                        bottom: 80,
                        left: 20,
                        right: 20,
                        child: _buildCaptionOverlay(),
                      ),

                      // Smart Word Definition Card
                      if (_activeSmartWord != null)
                        Positioned(
                          top: 40,
                          child: DefinitionCard(
                            word: _activeSmartWord!.text,
                            definition: _activeSmartWord!.definition ?? "",
                          ),
                        ),

                      // Waveform Overlay
                      Positioned(
                        bottom: 20,
                        left: 0,
                        right: 0,
                        child: WaveformVisualizer(isPlaying: _isPlaying),
                      ),

                      // Play/Pause Center Trigger
                      GestureDetector(
                        onTap: () {
                          setState(() {
                            _isPlaying ? _controller.pause() : _controller.play();
                            _isPlaying = !_isPlaying;
                          });
                        },
                        child: Container(
                          color: Colors.transparent,
                          child: Center(
                            child: _isPlaying ? null : Icon(Icons.play_arrow, size: 80, color: Colors.white.withOpacity(0.7)),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),

          // Action Tray
          Expanded(
            flex: 2,
            child: Container(
              decoration: const BoxDecoration(
                color: AppTheme.surfaceGray,
                borderRadius: BorderRadius.only(topLeft: Radius.circular(24), topRight: Radius.circular(24)),
              ),
              child: Column(
                children: [
                  _buildTabBar(),
                  Expanded(
                    child: IndexedStack(
                      index: _activeTabIndex,
                      children: [
                        _buildTimelineTab(),
                        _buildVocabularyTab(),
                        _buildStyleTab(),
                      ],
                    ),
                  ),
                  _buildScrubber(),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCaptionOverlay() {
    final words = widget.project.transcript ?? [];
    // Show a window of words around current time
    return Wrap(
      alignment: WrapAlignment.center,
      spacing: 8,
      runSpacing: 8,
      children: words.where((w) {
        final diff = (w.startTime - _currentPosition).inMilliseconds.abs();
        return diff < 2000;
      }).map((w) {
        final isCurrent = _currentPosition >= w.startTime && _currentPosition <= w.endTime;
        return Text(
          w.text,
          style: TextStyle(
            fontSize: isCurrent ? _fontSize : _fontSize * 0.75,
            fontWeight: isCurrent ? FontWeight.bold : FontWeight.normal,
            color: isCurrent ? AppTheme.linguaGold : Colors.white.withOpacity(0.7),
            backgroundColor: isCurrent ? Colors.black.withOpacity(_bgOpacity) : null,
          ),
        );
      }).toList(),
    );
  }

  Widget _buildTabBar() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          _TabItem(label: 'TIMELINE', icon: Icons.linear_scale, isActive: _activeTabIndex == 0, onTap: () => setState(() => _activeTabIndex = 0)),
          _TabItem(label: 'VOCABULARY', icon: Icons.auto_awesome, isActive: _activeTabIndex == 1, onTap: () => setState(() => _activeTabIndex = 1)),
          _TabItem(label: 'STYLE', icon: Icons.palette, isActive: _activeTabIndex == 2, onTap: () => setState(() => _activeTabIndex = 2)),
        ],
      ),
    );
  }

  Widget _buildTimelineTab() {
    final words = widget.project.transcript ?? [];
    return ListView.builder(
      scrollDirection: Axis.horizontal,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 20),
      itemCount: words.length,
      itemBuilder: (context, index) {
        final w = words[index];
        final isPassed = _currentPosition > w.endTime;
        return GestureDetector(
          onTap: () => _controller.seekTo(w.startTime),
          child: Container(
            margin: const EdgeInsets.only(right: 8),
            padding: const EdgeInsets.symmetric(horizontal: 12),
            alignment: Alignment.center,
            decoration: BoxDecoration(
              color: w.isSmartWord ? AppTheme.linguaGold.withOpacity(0.2) : Colors.white.withOpacity(0.05),
              borderRadius: BorderRadius.circular(8),
              border: Border.all(color: w.isSmartWord ? AppTheme.linguaGold : Colors.white12),
            ),
            child: Text(w.text, style: TextStyle(color: isPassed ? Colors.white54 : (w.isSmartWord ? AppTheme.linguaGold : Colors.white))),
          ),
        );
      },
    );
  }

  Widget _buildVocabularyTab() {
    final smartWords = widget.project.transcript?.where((w) => w.isSmartWord).toList() ?? [];
    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: smartWords.length,
      itemBuilder: (context, index) {
        final w = smartWords[index];
        return ListTile(
          leading: const Icon(Icons.check_circle, color: AppTheme.linguaGold),
          title: Text(w.text, style: const TextStyle(fontWeight: FontWeight.bold, color: AppTheme.linguaGold)),
          subtitle: Text(w.definition ?? "", style: const TextStyle(fontSize: 12)),
          trailing: IconButton(
            icon: const Icon(Icons.edit, size: 18),
            onPressed: () => _showEditDefinitionDialog(w),
          ),
        );
      },
    );
  }

  void _showEditDefinitionDialog(TranscriptionWord word) {
    final controller = TextEditingController(text: word.definition);
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        backgroundColor: AppTheme.surfaceGray,
        title: Text('Edit Definition: ${word.text}', style: const TextStyle(color: AppTheme.linguaGold)),
        content: TextField(
          controller: controller,
          maxLines: 3,
          decoration: const InputDecoration(border: OutlineInputBorder()),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: const Text('CANCEL')),
          ElevatedButton(
            onPressed: () {
              setState(() => word.definition = controller.text);
              Navigator.pop(context);
            },
            child: const Text('SAVE'),
          ),
        ],
      ),
    );
  }

  Widget _buildStyleTab() {
    return Padding(
      padding: const EdgeInsets.all(24.0),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('Font Size'),
              Text('${_fontSize.toInt()}px', style: const TextStyle(color: AppTheme.linguaGold)),
            ],
          ),
          Slider(
            value: _fontSize,
            min: 12,
            max: 48,
            onChanged: (v) => setState(() => _fontSize = v),
          ),
          const SizedBox(height: 16),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text('Bg Opacity'),
              Text('${(_bgOpacity * 100).toInt()}%', style: const TextStyle(color: AppTheme.linguaGold)),
            ],
          ),
          Slider(
            value: _bgOpacity,
            min: 0,
            max: 1,
            onChanged: (v) => setState(() => _bgOpacity = v),
          ),
        ],
      ),
    );
  }

  Widget _buildScrubber() {
    final total = _controller.value.duration;
    final current = _currentPosition;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Row(
        children: [
          Text(_formatDuration(current), style: const TextStyle(fontSize: 10, color: AppTheme.slateGray)),
          Expanded(
            child: Slider(
              value: total.inMilliseconds > 0 ? current.inMilliseconds.toDouble() : 0.0,
              max: total.inMilliseconds.toDouble(),
              onChanged: (v) => _controller.seekTo(Duration(milliseconds: v.toInt())),
            ),
          ),
          Text(_formatDuration(total), style: const TextStyle(fontSize: 10, color: AppTheme.slateGray)),
        ],
      ),
    );
  }

  String _formatDuration(Duration d) {
    final m = d.inMinutes;
    final s = d.inSeconds % 60;
    return '$m:${s.toString().padLeft(2, '0')}';
  }
}

class _TabItem extends StatelessWidget {
  final String label;
  final IconData icon;
  final bool isActive;
  final VoidCallback onTap;

  const _TabItem({required this.label, required this.icon, required this.isActive, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, color: isActive ? AppTheme.linguaGold : AppTheme.slateGray, size: 20),
          const SizedBox(height: 4),
          Text(label, style: TextStyle(color: isActive ? AppTheme.linguaGold : AppTheme.slateGray, fontSize: 10, fontWeight: isActive ? FontWeight.bold : FontWeight.normal)),
          if (isActive) Container(margin: const EdgeInsets.only(top: 4), height: 2, width: 20, color: AppTheme.linguaGold),
        ],
      ),
    );
  }
}
