import 'package:edu_play/models/project.dart';
import 'package:edu_play/services/project_provider.dart';
import 'package:edu_play/utils/app_theme.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:uuid/uuid.dart';

class CreateProjectScreen extends StatefulWidget {
  const CreateProjectScreen({super.key});

  @override
  State<CreateProjectScreen> createState() => _CreateProjectScreenState();
}

class _CreateProjectScreenState extends State<CreateProjectScreen> {
  final TextEditingController _nameController = TextEditingController();
  String? _imagePath;
  String? _audioPath;
  AspectRatioType _selectedRatio = AspectRatioType.tiktok;

  Future<void> _pickImage() async {
    final result = await FilePicker.platform.pickFiles(type: FileType.image);
    if (result != null) {
      setState(() => _imagePath = result.files.single.path);
    }
  }

  Future<void> _pickAudio() async {
    final result = await FilePicker.platform.pickFiles(type: FileType.audio);
    if (result != null) {
      setState(() => _audioPath = result.files.single.path);
    }
  }

  void _createProject() {
    if (_nameController.text.isEmpty || _imagePath == null || _audioPath == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please fill all fields and select media')),
      );
      return;
    }

    final newProject = Project(
      id: const Uuid().v4(),
      name: _nameController.text,
      imagePath: _imagePath!,
      audioPath: _audioPath!,
      aspectRatio: _selectedRatio,
      createdAt: DateTime.now(),
    );

    context.read<ProjectProvider>().addProject(newProject);
    Navigator.pushReplacementNamed(context, '/analyzing', arguments: newProject);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('New Project')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            TextField(
              controller: _nameController,
              decoration: const InputDecoration(
                labelText: 'Project Name',
                labelStyle: TextStyle(color: AppTheme.linguaGold),
                enabledBorder: UnderlineInputBorder(borderSide: BorderSide(color: AppTheme.slateGray)),
              ),
            ),
            const SizedBox(height: 32),
            Text('Aspect Ratio', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _RatioButton(
                  icon: Icons.smartphone,
                  label: '9:16',
                  isSelected: _selectedRatio == AspectRatioType.tiktok,
                  onTap: () => setState(() => _selectedRatio = AspectRatioType.tiktok),
                ),
                _RatioButton(
                  icon: Icons.tv,
                  label: '16:9',
                  isSelected: _selectedRatio == AspectRatioType.youtube,
                  onTap: () => setState(() => _selectedRatio = AspectRatioType.youtube),
                ),
                _RatioButton(
                  icon: Icons.crop_square,
                  label: '1:1',
                  isSelected: _selectedRatio == AspectRatioType.instagram,
                  onTap: () => setState(() => _selectedRatio = AspectRatioType.instagram),
                ),
              ],
            ),
            const SizedBox(height: 32),
            _MediaTile(
              title: 'Background Image',
              subtitle: _imagePath?.split('/').last ?? 'Select JPG/PNG',
              icon: Icons.image,
              onTap: _pickImage,
              isSet: _imagePath != null,
            ),
            const SizedBox(height: 16),
            _MediaTile(
              title: 'Audio Track',
              subtitle: _audioPath?.split('/').last ?? 'Select MP3/WAV',
              icon: Icons.audiotrack,
              onTap: _pickAudio,
              isSet: _audioPath != null,
            ),
            const SizedBox(height: 48),
            SizedBox(
              width: double.infinity,
              height: 56,
              child: ElevatedButton(
                onPressed: _createProject,
                child: const Text('CONTINUE TO AI ANALYSIS'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _RatioButton extends StatelessWidget {
  final IconData icon;
  final String label;
  final bool isSelected;
  final VoidCallback onTap;

  const _RatioButton({required this.icon, required this.label, required this.isSelected, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: isSelected ? AppTheme.linguaGold : AppTheme.surfaceGray,
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: isSelected ? AppTheme.linguaGold : AppTheme.slateGray),
            ),
            child: Icon(icon, color: isSelected ? Colors.black : Colors.white),
          ),
          const SizedBox(height: 8),
          Text(label, style: TextStyle(color: isSelected ? AppTheme.linguaGold : Colors.white70)),
        ],
      ),
    );
  }
}

class _MediaTile extends StatelessWidget {
  final String title;
  final String subtitle;
  final IconData icon;
  final VoidCallback onTap;
  final bool isSet;

  const _MediaTile({required this.title, required this.subtitle, required this.icon, required this.onTap, required this.isSet});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: onTap,
      contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      tileColor: AppTheme.surfaceGray,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12), side: BorderSide(color: isSet ? AppTheme.linguaGold : AppTheme.slateGray.withOpacity(0.3))),
      leading: Icon(icon, color: isSet ? AppTheme.linguaGold : Colors.white),
      title: Text(title, style: const TextStyle(fontWeight: FontWeight.bold)),
      subtitle: Text(subtitle, maxLines: 1, overflow: TextOverflow.ellipsis),
      trailing: Icon(isSet ? Icons.check_circle : Icons.chevron_right, color: isSet ? Colors.green : Colors.white54),
    );
  }
}
