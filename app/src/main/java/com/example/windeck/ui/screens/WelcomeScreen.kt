package com.example.windeck.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.models.AnswerOption
import com.example.windeck.ui.navigation.Screen
import com.example.windeck.ui.viewmodel.WelcomeViewModel
import com.example.windeck.ui.widgets.DialogueSystem
import com.example.windeck.ui.widgets.FinalText
import com.example.windeck.ui.widgets.MultipleChoice
import com.example.windeck.ui.widgets.percentOfScreenHeight
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode


@Composable
fun WelcomeScreen(navController: NavController) { // Bleibt der Einstiegspunkt
    val welcomeViewModel: WelcomeViewModel = viewModel()
    val response by welcomeViewModel.response.collectAsState()
    val responseSave by welcomeViewModel.responseSave.collectAsState()
    var kiresponse by rememberSaveable {  mutableStateOf("")}
    // Zustand: Welcher Teil wird gezeigt? false = Willkommen, true = KiPathChoice
    var showKiPathChoice by rememberSaveable { mutableStateOf(welcomeViewModel.getStartFromContent()) }
    var showMultipleChoiceTest by rememberSaveable { mutableStateOf(false) }
    var finalDecision by rememberSaveable { mutableStateOf(false) } //Decision if he wants to keep the KI Path or take the normal Path
    var showfinalDecision by rememberSaveable { mutableStateOf(false) }
    // --- Bedingtes Rendern basierend auf dem Zustand ---
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
    }
    if (!showKiPathChoice) {
        WelcomeContentInternal(
            onProceed = { subject ->
                welcomeViewModel.selectSubjectAndLoad(subject)
                println("Button geklickt, wechsle zu KiPathChoice.")
                showKiPathChoice = true
            }
        )
    } else if (!showMultipleChoiceTest){
        KiPathChoice(onProceed = {
            println("Button geklickt, wechsle zu MultipleChoice.")
            showMultipleChoiceTest = true
        }, standard = {
            welcomeViewModel.standardpath()
            println("Wurde abgespeichert?")
        })
    }
    else if(!finalDecision){
        MultipleChoice(onProceed = { answers ->
            welcomeViewModel.kipath(answers)
            println(answers)
            finalDecision = true // Zustand ändern, um UI zu wechseln
        })
    }
if(showfinalDecision)
{
    FinalText(
        listOf("Dein persönliches Ergebnis im Überblick:", kiresponse), answers = listOf(
            AnswerOption("Ja, ich möchte diese Reihenfolge haben!") {
                welcomeViewModel.kipathorder(
                    kiresponse
                )
            }, AnswerOption("Nein, ich möchte den Standard-Pfad haben!") {welcomeViewModel.standardpath()})
    )
}

    LaunchedEffect(response) {
        response?.let {
            when (it.status) {
                HttpStatusCode.OK -> {
                    val regex = Regex("""Reihenfolge:\[[^\]]+\]""")
                    val matchResult = regex.find(response!!.bodyAsText())
                    val gewünschterTextRegex = matchResult?.value
                    if (gewünschterTextRegex != null) {
                        println("Extrahierter Text (via Regex): $gewünschterTextRegex")
                        kiresponse += "Diese " + gewünschterTextRegex
                    } else {
                        println("Das Muster 'Reihenfolge:[...]' wurde nicht im Text gefunden.")
                    }

                    showfinalDecision = true
                }
                else -> {
                }
            }
        }
    }
    LaunchedEffect(responseSave) {
        responseSave?.let {
            when (it.status) {
                HttpStatusCode.OK -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.WelcomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
                else -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.WelcomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun WelcomeContentInternal(
    onProceed: (String) -> Unit
) {
    var isDialogue1Finished by remember { mutableStateOf(false) }
    var isDialogue2Finished by remember { mutableStateOf(false) }
    var showChoices by remember { mutableStateOf(false) }

    val welcomeDialogueLines = remember {
        listOf(
            "Hey! Willkommen an Bord!",
            "Mit dieser App powerst du dein Studiumwissen auf!"
        )
    }
    val choiceDialogueLine = remember {
        listOf("Wähle dein Fach und leg los:")
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
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )

        if (!isDialogue1Finished) {
            DialogueSystem(
                modifier = Modifier.fillMaxSize(),
                dialogueLines = welcomeDialogueLines,
                onDialogueComplete = {
                    isDialogue1Finished = true
                    println("Dialog 1 beendet!")
                }
            )
        }

        if (isDialogue1Finished) {
            DialogueSystem(
                modifier = Modifier.fillMaxSize(),
                dialogueLines = choiceDialogueLine,
                contentAlignment = Alignment.TopCenter,
                topPaddingIfTopAligned = percentOfScreenHeight(0.1f),
                onDialogueComplete = {
                    isDialogue2Finished = true
                    showChoices = true
                    println("Dialog 2 (Auswahl) Text beendet!")
                }
            )
        }

        /* --- Auswahl-Buttons --- */
        if (showChoices) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = percentOfScreenHeight(0.3f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mathe Button
                Button(
                    onClick = {
                        onProceed("math")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(110.dp)
                        .paint(
                            painter = painterResource(id = R.drawable.buttonverlauf),
                            contentScale = ContentScale.FillBounds
                        ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Mathe",
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onProceed("java")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(110.dp)
                        .paint(
                            painter = painterResource(id = R.drawable.buttonverlauf),
                            contentScale = ContentScale.FillBounds
                        ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Java",
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


@Composable
fun KiPathChoice(onProceed: () -> Unit, standard: () -> Unit) {
    var isDialogue1Finished by remember { mutableStateOf(false) }
    var isDialogue2Finished by remember { mutableStateOf(false) }
    var showChoices by remember { mutableStateOf(false) }
    var hideDialogueAfterChoice by remember { mutableStateOf(false) }

    val choiceDialogueLine = remember {
        listOf("Möchtest du einen auf dich angepassten Pfad haben? (KI)")
    }
    val afterchoiceDialogueLine = remember {
        listOf("Zeit für ein kleines Quiz! 6 Fragen, um genau zu sein.",
            "Musst nicht alles wissen – dafür sind wir ja da!",
            "Starten wir mit Frage 1:")
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Image( painter = painterResource(R.drawable.basiccolor), // Ersetzen
            contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier.matchParentSize() )
        Image( painter = painterResource(R.drawable.backgroundfinal),
            contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.matchParentSize() )

        if (!hideDialogueAfterChoice) {
            DialogueSystem(
                modifier = Modifier.fillMaxSize(),
                dialogueLines = choiceDialogueLine,
                contentAlignment = Alignment.TopCenter,
                topPaddingIfTopAligned = percentOfScreenHeight(0.1f),
                onDialogueComplete = {
                    isDialogue1Finished = true
                    showChoices = true
                    println("Dialog 2 (Auswahl) Text beendet!")
                }
            )
        }

        if (showChoices && !hideDialogueAfterChoice) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = percentOfScreenHeight(0.3f)),
            ) {
                Button(
                    onClick = { hideDialogueAfterChoice = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),

                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(140.dp)
                        .paint(
                            painterResource(R.drawable.buttonverlauf),
                            contentScale = ContentScale.FillBounds
                        ),
                ) {
                    Text(
                        text = "Ja, ich möchte einen Test abschließen und einen angepassten Pfad haben!",
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = {  standard() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),

                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(140.dp)
                        .paint(
                            painterResource(R.drawable.buttonverlauf),
                            contentScale = ContentScale.FillBounds    // skaliert das Bild flächendeckend
                        ),
                ) {
                    Text(
                        text = "Nein, ich möchte den Standard-Pfad haben!",
                        modifier = Modifier.padding(bottom = 6.dp),
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if(hideDialogueAfterChoice && isDialogue1Finished && !isDialogue2Finished)
        {
            DialogueSystem(
                modifier = Modifier.fillMaxSize(),
                dialogueLines = afterchoiceDialogueLine,
                contentAlignment = Alignment.TopCenter,
                topPaddingIfTopAligned = percentOfScreenHeight(0.1f),
                // ----------------------------------
                onDialogueComplete = {
                    isDialogue2Finished = true
                    println("Dialog 2 (Auswahl) Text beendet!")
                }
            )
        }
        if(isDialogue2Finished)
        {
            onProceed()
        }
    }
}


