package com.example.windeck.ui.viewmodel

import ApiClientStyled
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windeck.ui.data.Content
import com.example.windeck.ui.data.Repository
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val _response = MutableStateFlow<HttpResponse?>(null)
    val response: StateFlow<HttpResponse?> get() = _response

    private val _serverAvailability = MutableStateFlow<Boolean?>(null)
    val serverAvailability: StateFlow<Boolean?> get() = _serverAvailability

    fun register(username: String, password: String) {
        ApiClientStyled.ownerUsername = username.lowercase()
        Content.start = false
        viewModelScope.launch {
            val result = Repository.register(username, password)
            _response.value = result // Setze die Antwort (kann null sein)

            // NEU: Wenn das Ergebnis null ist (Fehler aufgetreten),
            // setze die Server-Verfügbarkeit auf false.
            if (result == null) {
                _serverAvailability.value = false
            }
            // Optional: Bei Erfolg auf true setzen? Eher nicht, response regelt den Erfolgsfall.
        }
    }
    fun login(username: String, password: String) {
        ApiClientStyled.ownerUsername = username.lowercase()
        Content.start = false
        viewModelScope.launch {
            val result = Repository.login(username, password)
            _response.value = result // Setze die Antwort (kann null sein)

            // NEU: Wenn das Ergebnis null ist (Fehler aufgetreten),
            // setze die Server-Verfügbarkeit auf false.
            if (result == null) {
                _serverAvailability.value = false
            }
            // Optional: Bei Erfolg auf true setzen? Eher nicht, response regelt den Erfolgsfall.
        }
    }

    fun clearResponse() {
        _response.value = null
    }
    fun clearServer() {
        _serverAvailability.value = null
    }
}