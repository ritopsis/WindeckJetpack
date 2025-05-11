package com.example.windeck.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windeck.ui.data.Content
import com.example.windeck.ui.data.Repository
import kotlinx.coroutines.launch

class ChapterViewModel : ViewModel() {

    fun loadChapterContent()
    {
        Content.loadChapter()
    }
    fun getChapterContent() : List<String>
    {
        return Content.contentOfChapter
    }
    fun checkAnswer(answer : String): Boolean
    {
        if (Content.question != null)
        {
            if(answer.contains(Content.question[0].correctAnwsers[0]))
            {
                return true
            }
        }
        return false
    }
    fun saveFinishedChapter()
    {
        viewModelScope.launch {
            Repository.saveFinishedChapter(Content.subject, Content.chapter.toString())
        }
    }
}