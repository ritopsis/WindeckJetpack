package com.example.windeck.ui.navigation

sealed class Screen(val route: String) {

    object StartScreen : Screen("start")
    object LoginScreen : Screen("login")
    object RegistrationScreen : Screen("registration")
    object WelcomeScreen : Screen("welcome")
    object MainScreen : Screen("main")
    object RankingScreen : Screen("ranking")
    object ProfileScreen : Screen("profile")
    object ChapterScreen : Screen("chapter")
    object QuizScreen : Screen("quiz")
}