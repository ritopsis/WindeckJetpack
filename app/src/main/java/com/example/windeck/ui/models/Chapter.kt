package com.example.windeck.ui.models

data class Chapter(
    val id: Double,
    val topic: String,
    val title: String,
    val lockedIcon: Any? = null,
    val unlockedIcon: Any? = null,
    val completedIcon: Any? = null
)