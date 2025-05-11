package com.example.windeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windeck.ui.data.Repository
import com.example.windeck.ui.data.TopUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {
    private val _response = MutableStateFlow<List<TopUser>?>(null)
    val response: StateFlow<List<TopUser>?> get() = _response


    fun getAllUserAchievements() {
        viewModelScope.launch {
            val result = Repository.getTopAchievementUsers()
            _response.value = result // Setze die Antwort (kann null sein)
        }
    }
}