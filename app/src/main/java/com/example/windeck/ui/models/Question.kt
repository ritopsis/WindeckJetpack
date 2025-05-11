package com.example.windeck.ui.models

data class Question(
    val topic: String,
    val question: String,
    val allAnwsers: List<String>,
    val correctAnwsers: List<String> = emptyList()
)