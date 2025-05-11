package com.example.windeck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.windeck.ui.screens.ChapterScreen
import com.example.windeck.ui.screens.LoginScreen
import com.example.windeck.ui.screens.MainScreen
import com.example.windeck.ui.screens.ProfileScreen
import com.example.windeck.ui.screens.QuizScreen
import com.example.windeck.ui.screens.RankingScreen
import com.example.windeck.ui.screens.RegistrationScreen
import com.example.windeck.ui.screens.StartScreen
import com.example.windeck.ui.screens.WelcomeScreen
import com.example.windeck.ui.theme.WindeckTheme
import com.example.windeck.ui.viewmodel.AuthViewModel


@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route
    ) {
        composable(route = Screen.StartScreen.route) {
            StartScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = Screen.RegistrationScreen.route) {
            RegistrationScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = Screen.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = Screen.RankingScreen.route) {
            RankingScreen(navController = navController)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screen.ChapterScreen.route) {
            ChapterScreen(navController = navController)
        }
        composable(route = Screen.QuizScreen.route) {
            QuizScreen(navController = navController)
        }
    }

}