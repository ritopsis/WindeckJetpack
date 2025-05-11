package com.example.windeck.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.models.AnswerOption
import com.example.windeck.ui.navigation.Screen
import com.example.windeck.ui.widgets.DialogueSystem
import com.example.windeck.ui.widgets.FinalText
import com.example.windeck.ui.widgets.MultipleChoice
import com.example.windeck.ui.widgets.percentOfScreenHeight

@Composable
fun QuizScreen(navController: NavController) {
    var showDialoag by rememberSaveable { mutableStateOf(false) }
    var showMultipleChoiceTest by rememberSaveable { mutableStateOf(false) }


    if (!showDialoag) {
        QuizScreenContent({showDialoag = true})
    } else if (!showMultipleChoiceTest){
        MultipleChoice(onProceed = {
            println("Button geklickt, wechsle zu MultipleChoice.")
            showMultipleChoiceTest = true
        })
    }
    else
    {
        FinalText(
            listOf("Dein persönliches Ergebnis im Überblick:"), answers = listOf(
                AnswerOption("Zurück zum Hauptmenü", {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.MainScreen.route) {
                            inclusive = true
                        }}
                }))
            ,0.6f
        )
    }
}
@Composable
fun QuizScreenContent(onProceed: () -> Unit)  {
    var isDialogue1Finished by remember { mutableStateOf(false) }

    val welcomeDialogueLines = remember {
        listOf(
            "Dir werden nun Fragen gestellt!",
            "Danach erhälst du ein Feedback von der KI welche Kapitel du genauer anschauen solltest."
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.basiccolor),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.backgroundfinal),
            contentDescription = null,
            contentScale = ContentScale.Fit, // Beachte Fit vs FillBounds je nach Bild
            modifier = Modifier.matchParentSize()
        )
        if (!isDialogue1Finished) {
            DialogueSystem(
                modifier = Modifier.fillMaxSize(), // Füllt den gesamten Box-Bereich
                dialogueLines = welcomeDialogueLines,
                onDialogueComplete = {
                    isDialogue1Finished = true
                    onProceed()
                    println("Dialog 1 beendet!")
                },
                contentAlignment = Alignment.TopCenter,
                topPaddingIfTopAligned = percentOfScreenHeight(0.1f), 
            )
        }
    }
}