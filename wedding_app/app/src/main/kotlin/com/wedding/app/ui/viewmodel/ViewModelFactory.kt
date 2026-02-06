package com.wedding.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wedding.app.data.repository.WeddingRepository

class ViewModelFactory(private val repository: WeddingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeddingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeddingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
