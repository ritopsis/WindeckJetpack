package com.example.windeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windeck.ui.data.Content
import com.example.windeck.ui.data.Repository
import com.example.windeck.ui.models.Chapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    // NEU: StateFlow für das Subject
    private val _subjectState = MutableStateFlow<String?>(null) // Startet mit null (oder einem Defaultwert)
    val subjectState: StateFlow<String?> = _subjectState // Öffentlicher, nur lesbarer StateFlow

    // Optional: StateFlow für die sortierten Kapitel, falls die UI sie braucht
    private val _orderedChaptersState = MutableStateFlow<List<Chapter>>(emptyList())
    val orderedChaptersState: StateFlow<List<Chapter>> = _orderedChaptersState

    // Optional: StateFlow für die sortierten Kapitel, falls die UI sie braucht
    private val _finishedchapters = MutableStateFlow<List<Double>?>(emptyList())
    val finishedchapters: StateFlow<List<Double>?> = _finishedchapters

    private val _subjectCheckStatus = MutableStateFlow<Boolean?>(null)
    val subjectCheckStatus: StateFlow<Boolean?> = _subjectCheckStatus

    private val _kiFeedback = MutableStateFlow<String?>(null)
    val kiFeedback: StateFlow<String?> = _kiFeedback

    fun kiQuizFeedback(answers : String)
    {
        viewModelScope.launch {
            _kiFeedback.value = Repository.kiFeedback(answers)
        }
    }

    fun lastusedsubject() {
        viewModelScope.launch {
            val subjectInfo = Repository.getLastUsedSubject()
            if (subjectInfo != null)
            {
                Content.subject = subjectInfo.subject // Globales Objekt ggf. weiter setzen
                _subjectState.value = Content.fullSubjectName()// <<< WICHTIG: StateFlow aktualisieren
                //val blub = Repository.getAllFinishedChapters(Content.subject)
                // Kapitel laden und sortieren
                _finishedchapters.value = Repository.getAllFinishedChapters(Content.subject)
                Content.loadChapterOverview() // Annahme: Lädt in Content.chapters
                val order: List<Int> = subjectInfo.sequence
                    .split(';')
                    .mapNotNull { it.trim().toIntOrNull() }
                val orderedChapters: List<Chapter> = Content.chapters.sortedWith(
                    compareBy<Chapter> { chapter ->
                        order.indexOf(chapter.id.toInt())
                    }
                        .thenBy { chapter -> chapter.id }
                )
                _orderedChaptersState.value = orderedChapters // Optional: Kapitel-State aktualisieren
            }
            else {
                // Fehlerfall: Subject konnte nicht geladen werden
                _subjectState.value = null // Oder einen Fehlerwert setzen
            }
        }
    }
    fun startChapter(chapterid: Double)
    {
        Content.chapter = chapterid
    }
    fun selectSubjectAndLoad(subject: String) {
        Content.subject = subject
        viewModelScope.launch {
            Repository.updateLastUsedSubject(Content.subject)
            try {
                Content.startLoadingQuestions(subject)
            } catch (e: Exception) {
                println("ViewModel: Fehler beim Laden für '$subject': ${e.message}")
            } finally {
            }
        }
    }
    fun checkByChangeSubject()
    {
        Content.start = true
        viewModelScope.launch {

            val responseSequence = Repository.getAllFinishedChapters(Content.subject)
            if (responseSequence == null)
            {
                _subjectCheckStatus.value = true
            }
            else
            {
                _subjectCheckStatus.value = false
            }
        }
    }

    fun loadQuestions()
    {
        Content.startLoadingQuestions(Content.subject)
    }
    fun getNextUnlockableChapterId(
        allChaptersOrdered: List<Chapter>, // Die korrekte Reihenfolge für das aktuelle Fach
        finishedChapterIDs: List<Double>?
    ): Double? {
        if (allChaptersOrdered.isEmpty()) return null

        val finishedSet = finishedChapterIDs?.toSet() ?: emptySet()

        // Wenn noch gar nichts abgeschlossen wurde, ist das erste Kapitel der Liste das nächste.
        if (finishedSet.isEmpty()) {
            return allChaptersOrdered.firstOrNull()?.id
        }

        // Finde den Index des letzten Kapitels in 'allChaptersOrdered', das abgeschlossen wurde.
        var highestFinishedIndexInCurrentOrder = -1
        allChaptersOrdered.forEachIndexed { index, chapter ->
            if (finishedSet.contains(chapter.id)) {
                highestFinishedIndexInCurrentOrder = index
            }
        }

        // Wenn kein abgeschlossenes Kapitel in der aktuellen Reihenfolge gefunden wurde
        // (obwohl finishedSet nicht leer ist), bedeutet das, für dieses Fach fängt man von vorne an.
        if (highestFinishedIndexInCurrentOrder == -1) {
            return allChaptersOrdered.firstOrNull()?.id
        }

        // Wenn das letzte abgeschlossene Kapitel das letzte in der gesamten Liste ist, gibt es kein "nächstes".
        if (highestFinishedIndexInCurrentOrder == allChaptersOrdered.size - 1) {
            return null
        }

        // Das nächste freizuschaltende Kapitel ist das direkt folgende in der sortierten Liste.
        return allChaptersOrdered[highestFinishedIndexInCurrentOrder + 1].id
    }

}