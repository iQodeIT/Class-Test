package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.soulstice.app.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    val theme = preferenceManager.theme
}
