package com.example.windeck.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.windeck.R
import com.example.windeck.ui.models.AnswerOption
import kotlinx.coroutines.delay

@Composable
fun FinalText(dialogueLines : List<String>, answers : List<AnswerOption>, abstand : Float = 0.3f, size : TextUnit = 25.sp) {
    var showAnswerButtons by remember { mutableStateOf(false) }

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
        DialogueSystem(
            modifier = Modifier.fillMaxSize(),
            dialogueLines = dialogueLines,
            contentAlignment = Alignment.TopCenter,
            topPaddingIfTopAligned = percentOfScreenHeight(0.1f),
            onDialogueComplete = {
                showAnswerButtons = true
            },
            size = size
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = percentOfScreenHeight(abstand)),
            contentAlignment = Alignment.TopCenter
        ) {
            if (showAnswerButtons) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    answers.forEach { option ->
                        println(option.text)
                        Button(
                            onClick = { option.onClick() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .paint(
                                    painterResource(R.drawable.buttonverlauf),
                                    contentScale = ContentScale.FillBounds
                                ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = option.text,
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
        }
    }
}
@Composable
fun loadingBackground()
{
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
    }
}

@Composable
fun DialogueSystem(
    modifier: Modifier = Modifier,
    dialogueLines: List<String>,
    animationDelayMillis: Long = 40L,
    contentAlignment: Alignment = Alignment.Center,
    topPaddingIfTopAligned: Dp = 0.dp, // Standardmäßig kein extra Top-Padding
    // ---------------------
    onDialogueComplete: () -> Unit,
    size: TextUnit = 25.sp
) {
    var currentLineIndex by remember { mutableIntStateOf(0) }
    var showDialogueContent by remember { mutableStateOf(true) }
    var isAnimationComplete by remember { mutableStateOf(false) }
    var skipTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentLineIndex, showDialogueContent) {
        if (showDialogueContent) {
            isAnimationComplete = false
        }
    }

    if (showDialogueContent && currentLineIndex < dialogueLines.size) {
        Box(
            modifier = modifier // Der Container füllt den ihm gegebenen Raum
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (currentLineIndex < dialogueLines.size) {
                        if (!isAnimationComplete) {
                            skipTrigger++
                        } else {
                            if (currentLineIndex < dialogueLines.size - 1) {
                                currentLineIndex++
                            } else {
                                onDialogueComplete()
                                // Sichtbarkeit wird nun primär durch die Elternkomponente gesteuert,
                                // besonders wenn keepVisibleAfterComplete true ist.
                            }
                        }
                    }
                }
        ) {
            // --- MODIFIZIERTE STELLE ---
            // Berechne das tatsächliche Padding basierend auf der Ausrichtung
            val topPadding = if (contentAlignment == Alignment.TopCenter) {
                topPaddingIfTopAligned // Nutze das spezifische Padding
            } else {
                0.dp // Kein zusätzliches Top-Padding bei anderer Ausrichtung
            }

            // Wende zuerst die Ausrichtung an, dann das Padding
            GameStyleTypewriterTextV2(
                modifier = Modifier
                    .align(contentAlignment) // 1. Text-Box ausrichten
                    .padding(top = topPadding) // 2. Inhalt innerhalb der Box nach unten schieben (falls top aligned)
                    .padding(horizontal = 24.dp), // 3. Vorhandenes horizontales Padding beibehalten
                fullText = dialogueLines[currentLineIndex],
                skipTrigger = skipTrigger,
                onAnimationStateChange = { completed ->
                    isAnimationComplete = completed
                },
                animationDelayMillis = animationDelayMillis,
                size = size
            )
        }
    }
}


@Composable
fun GameStyleTypewriterTextV2(
    modifier: Modifier = Modifier,
    fullText: String,
    skipTrigger: Int,
    onAnimationStateChange: (isComplete: Boolean) -> Unit,
    animationDelayMillis: Long = 50L,
    size: TextUnit = 25.sp
) {
    var displayedText by remember(fullText) { mutableStateOf("") }
    var textIndex by remember(fullText) { mutableIntStateOf(0) }
    var internalAnimationComplete by remember(fullText) { mutableStateOf(false) }

    // Effekt zum "Tippen"
    LaunchedEffect(key1 = fullText) {
        internalAnimationComplete = false
        onAnimationStateChange(false)
        textIndex = 0
        displayedText = ""

        while (textIndex < fullText.length) {
            // Frühzeitiger Abbruch durch Skip-Effekt prüfen (optional, aber gut)
            if (internalAnimationComplete) break // Verhindert unnötigen delay nach Skip

            displayedText += fullText[textIndex]
            textIndex++
            delay(animationDelayMillis)
        }

        // Nur als abgeschlossen melden, wenn die Schleife natürlich endete
        if (!internalAnimationComplete && textIndex == fullText.length) {
            internalAnimationComplete = true
            onAnimationStateChange(true)
        }
    }

    // Effekt für Skip-Trigger
    LaunchedEffect(key1 = skipTrigger) {
        // Nur reagieren, wenn Trigger > 0 (also ausgelöst wurde) UND die Animation noch nicht fertig ist
        if (skipTrigger > 0 && !internalAnimationComplete) {
            textIndex = fullText.length
            displayedText = fullText
            internalAnimationComplete = true
            onAnimationStateChange(true) // Animation ist jetzt (durch Skip) beendet
        }
    }

    Box(
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = displayedText,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = size,
            )
        }
    }
}