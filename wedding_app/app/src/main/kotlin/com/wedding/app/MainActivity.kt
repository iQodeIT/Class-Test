package com.wedding.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wedding.app.data.local.WeddingDatabase
import com.wedding.app.data.repository.WeddingRepository
import com.wedding.app.ui.navigation.WeddingNavGraph
import com.wedding.app.ui.theme.WeddingAppTheme
import com.wedding.app.ui.viewmodel.ViewModelFactory
import com.wedding.app.ui.viewmodel.WeddingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = WeddingDatabase.getDatabase(this)
        val repository = WeddingRepository(database.weddingDao())
        val factory = ViewModelFactory(repository)

        setContent {
            WeddingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: WeddingViewModel = viewModel(factory = factory)
                    WeddingNavGraph(viewModel)
                }
            }
        }
    }
}
