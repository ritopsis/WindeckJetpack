package com.example.windeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windeck.ui.data.Achievement
import com.example.windeck.ui.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _response = MutableStateFlow<List<Achievement>?>(null)
    val response: StateFlow<List<Achievement>?> get() = _response


    fun getAchievements() {
        viewModelScope.launch {
            val result = Repository.getAllUserAchievements()
            _response.value = result // Setze die Antwort (kann null sein)
        }
    }

    fun getUsername(): String
    {
        return ApiClientStyled.ownerUsername
    }
}