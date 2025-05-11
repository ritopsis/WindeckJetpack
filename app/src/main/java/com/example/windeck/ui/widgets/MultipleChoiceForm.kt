package com.example.windeck.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.windeck.R
import com.example.windeck.ui.data.Content
import com.example.windeck.ui.models.Question
@Composable
fun MultipleChoice(onProceed: (String) -> Unit, questionlist : List<Question> = Content.question) {

    var allanswers by remember { mutableStateOf("") }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var hidequestions by remember { mutableStateOf(false) }
    var showAnswerButtons by remember { mutableStateOf(false) }
    val questions = remember {
        questionlist
    }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.basiccolor), // Ersetzen
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(R.drawable.backgroundfinal), // Ersetzen
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )

        if (currentQuestion != null) {
            if (!showAnswerButtons || !hidequestions) {
                DialogueSystem(
                    modifier = Modifier.fillMaxSize(),
                    dialogueLines = listOf(currentQuestion.question),
                    contentAlignment = Alignment.TopCenter,
                    topPaddingIfTopAligned = percentOfScreenHeight(0.1f),
                    onDialogueComplete = {
                        println("Frage ${currentQuestionIndex + 1} angezeigt.")
                        showAnswerButtons = true
                    }
                )
            }

            if (showAnswerButtons && !hidequestions) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = percentOfScreenHeight(0.3f)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val answers = currentQuestion.allAnwsers.take(4)
                    answers.forEachIndexed { index, answerText ->
                        Button(
                            onClick = {
                                println("Antwort ${index + 1} für Frage ${currentQuestionIndex + 1} geklickt.")
                                allanswers += "${currentQuestion.topic}) Frage: ${currentQuestion.question} Antwort vom User: $answerText "
                                // 1. Hide buttons immediately
                                showAnswerButtons = false
                                // 2. Move to the next question (if available)
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                } else {
                                    // 3. Quiz Finished - Navigate or show completion message
                                    println("Quiz beendet!")
                                    onProceed(allanswers)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(140.dp)
                                .paint(
                                    painterResource(R.drawable.buttonverlauf), // Ersetzen
                                    contentScale = ContentScale.FillBounds
                                ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = answerText,
                                fontSize = 16.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(bottom = 6.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // currentQuestionIndex is out of bounds
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (questions.isEmpty()) "Keine Fragen geladen." else "Quiz beendet!",
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


