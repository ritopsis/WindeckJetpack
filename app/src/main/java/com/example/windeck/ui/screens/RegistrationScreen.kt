package com.example.windeck.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.windeck.ui.viewmodel.AuthViewModel
import com.example.windeck.ui.widgets.AuthForm

@Composable
fun RegistrationScreen(navController: NavController, authViewModel: AuthViewModel) {
    AuthForm(navController, register = true, authViewModel = authViewModel)
}

