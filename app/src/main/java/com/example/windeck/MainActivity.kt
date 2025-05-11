package com.example.windeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.windeck.ui.navigation.Navigation
import com.example.windeck.ui.theme.WindeckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WindeckTheme { // Wende hier dein Theme an
                Navigation() // Deine Navigations-Composable oder Haupt-Screen
            }
        }
    }
}