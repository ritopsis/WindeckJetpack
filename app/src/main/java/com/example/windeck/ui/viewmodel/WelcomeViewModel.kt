package com.example.windeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windeck.ui.data.Content
import com.example.windeck.ui.data.Repository
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel: ViewModel() {
    private val _response = MutableStateFlow<HttpResponse?>(null)
    val response: StateFlow<HttpResponse?> get() = _response

    private val _responseSave = MutableStateFlow<HttpResponse?>(null)
    val responseSave: StateFlow<HttpResponse?> get() = _responseSave

    fun kipath(answers: String) {
        viewModelScope.launch {
            val result = Repository.kiPath(answers)
            _response.value = result // Setze die Antwort (kann null sein)
        }
    }
    fun savepath(sequence: String)
    {
        viewModelScope.launch {
            val result = Repository.saveSubjectSequence(Content.subject,sequence)
            _responseSave.value = result // Setze die Antwort (kann null sein)
        }
    }
    fun getStartFromContent(): Boolean
    {
        return Content.start
    }
    fun standardpath()
    {
        savepath(Content.getStandardSequence())
    }
    fun kipathorder(order: String) {
        val regex = Regex("""\[[^\]]+\]""") // Sucht nach "[...]"
        val matchResult = regex.find(order) // Finde das MatchResult-Objekt

        // Prüfe, ob ein Match gefunden wurde und extrahiere den .value
        val matchedText = matchResult?.value // .value enthält den gefundenen String, z.B. "[Vektoren,Matrizen,Komplexe Zahlen]"

        if (matchedText != null) {
            val reihenfolge = getSequenceInNumbers(matchedText)
            savepath(reihenfolge)
        } else {
        }
    }
    fun getSequenceInNumbers(input: String): String {
        // Entferne führende '[' und folgende ']'
        // .trim() entfernt nur Leerzeichen, .trim('[', ']') entfernt die spezifischen Zeichen
        val trimmedInput = input.trim('[', ']')

        // Wähle die Verarbeitung basierend auf dem Fach (ignoriert Groß-/Kleinschreibung)
        val result = when (Content.subject.lowercase()) {
            "java" -> {
                // Mapping für Java-Themen
                val mapping = mapOf(
                    "Variablen" to 1,
                    "Schleifen" to 2,
                    "OOP-Grundlagen" to 3
                    // Füge hier weitere Java-Themen hinzu
                )
                processInput(trimmedInput, mapping) // Rufe die Hilfsfunktion auf
            }
            "math" -> {
                // Mapping für Mathe-Themen
                val mapping = mapOf(
                    "Komplexe Zahlen" to 1,
                    "Vektoren" to 2,
                    "Matrizen" to 3
                    // Füge hier weitere Mathe-Themen hinzu
                )
                processInput(trimmedInput, mapping) // Rufe die Hilfsfunktion auf
            }
            else -> {
                println("Unbekanntes Fach: ${Content.subject}")
                "" // Gib einen leeren String zurück, wenn das Fach nicht erkannt wird
            }
        }

        println("Output: $result") // Ersetzt Debug.Log
        return result
    }
    private fun processInput(trimmedInput: String, mapping: Map<String, Int>): String {
        return trimmedInput
            .split(',') // Teilt den String am Komma in eine Liste auf
            .map { it.trim() } // Entfernt führende/folgende Leerzeichen von jedem Teil
            .mapNotNull { mapping[it] } // Sucht jeden Teil in der Map; ignoriert Teile, die nicht gefunden werden (gibt null zurück -> wird durch mapNotNull entfernt)
            .joinToString(";") // Verbindet die gefundenen Zahlen (Int) mit einem Komma zu einem String
    }
    fun selectSubjectAndLoad(subject: String) {
        Content.subject = subject
        viewModelScope.launch {
            try {
                Content.startLoadingQuestions(subject)

            } catch (e: Exception) {
                println("ViewModel: Fehler beim Laden für '$subject': ${e.message}")
            } finally {
            }
        }
    }
}