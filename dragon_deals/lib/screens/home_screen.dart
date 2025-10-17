import 'package:flutter/material.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 6, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Dragon Deals'),
        bottom: TabBar(
          controller: _tabController,
          isScrollable: true,
          tabs: const [
            Tab(text: '1688.com'),
            Tab(text: 'YiwuGo.com'),
            Tab(text: 'Taobao.com'),
            Tab(text: 'Made-in-China.com'),
            Tab(text: 'SMZDM.com'),
            Tab(text: 'DHgate.com'),
          ],
        ),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              decoration: InputDecoration(
                hintText: 'Search for products...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20.0),
                ),
              ),
            ),
          ),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: const [
                Center(child: Text('1688.com Results')),
                Center(child: Text('YiwuGo.com Results')),
                Center(child: Text('Taobao.com Results')),
                Center(child: Text('Made-in-China.com Results')),
                Center(child: Text('SMZDM.com Results')),
                Center(child: Text('DHgate.com Results')),
              ],
            ),
          ),
        ],
      ),
    );
  }
}