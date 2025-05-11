package com.example.windeck.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.models.AnswerOption
import com.example.windeck.ui.models.Chapter
import com.example.windeck.ui.navigation.Screen
import com.example.windeck.ui.theme.MyFontFamily
import com.example.windeck.ui.viewmodel.MainViewModel
import com.example.windeck.ui.widgets.FinalText
import com.example.windeck.ui.widgets.MultipleChoice
import com.example.windeck.ui.widgets.SimpleBottomAppBar
import com.example.windeck.ui.widgets.loadingBackground
import com.example.windeck.ui.widgets.percentOfScreenHeight
import kotlin.math.floor
import kotlin.math.max

@Composable
fun MainScreen(navController: NavController) {
    val mainViewModel : MainViewModel = viewModel()
    var changesubject by rememberSaveable { mutableStateOf(false) }
    val finishedchapterlist by mainViewModel.finishedchapters.collectAsState()
    val firstTimeSubject by mainViewModel.subjectCheckStatus.collectAsState()
    val changeSubjectShow = {changesubject = !changesubject}
    val subject by mainViewModel.subjectState.collectAsState()
    val chapters by mainViewModel.orderedChaptersState.collectAsState()

    var quizshow by remember { mutableStateOf(false) }
    // LaunchedEffect nur noch, um das Laden *einmalig anzustoßen*
    LaunchedEffect(Unit) {
        mainViewModel.lastusedsubject()
    }
    loadingBackground()
    Box(modifier = Modifier.fillMaxSize()) {

        if (!quizshow) {
            if (!changesubject) {
                subject?.let { currentSubject ->
                    if (chapters != null && !changesubject) {
                        MainScreenContent(
                            modifier = Modifier.padding(),
                            navController = navController,
                            currentSubject,
                            chapters,
                            changeSubjectShow,
                            finishedchapterlist,
                            mainViewModel.getNextUnlockableChapterId(chapters, finishedchapterlist),
                            { chapterid ->
                                mainViewModel.startChapter(chapterid)
                                navController.navigate(Screen.ChapterScreen.route)
                            },
                            {
                                quizshow = true;
                            })
                    }
                }
            }
            if (changesubject) {
                ChangeSubjectContent(navController = navController, { subject ->
                    mainViewModel.selectSubjectAndLoad(subject)
                    mainViewModel.checkByChangeSubject()
                }, changeSubjectShow)
            }
            if (firstTimeSubject != null) {
                if (firstTimeSubject == true) {
                    navController.navigate(Screen.WelcomeScreen.route)
                    {
                        popUpTo(Screen.MainScreen.route) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate(Screen.MainScreen.route)
                    {
                        popUpTo(Screen.MainScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                SimpleBottomAppBar(navController = navController)
            }
        }else if (quizshow)
        {
            quiz(navController,mainViewModel)
        }
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MainScreenContent(
    modifier: Modifier,
    navController: NavController,
    subject: String,
    list: List<Chapter>,
    changesubject: () -> Unit,
    finishedchapterlist : List<Double>?,
    nextunlock : Double?,
    startChapter: (Double) -> Unit,
    quiz : () -> Unit
) {
    var selectedChapter by remember { mutableStateOf<Chapter?>(null) }
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.basiccolor),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        Image(
            painter = painterResource(id = R.drawable.mainbackground),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = percentOfScreenHeight(0.22f))
                .padding(horizontal = 2.dp)
        ) {
            val availableWidth = maxWidth
            val horizontalOffsetStep = 60.dp
            val endPadding = 40.dp

            val stepsBeforeReverse = if (horizontalOffsetStep > 0.dp && availableWidth > endPadding + horizontalOffsetStep) {
                // Berechne den maximal möglichen Offset
                val maxAllowedOffset = availableWidth - endPadding
                // Berechne, wie viele Schritte hineinpassen (abrunden) und addiere 1
                val calculatedSteps = floor(maxAllowedOffset.value / horizontalOffsetStep.value).toInt() + 1
                // Stelle sicher, dass es mindestens 2 Schritte sind für Zickzack
                max(2, calculatedSteps)
            } else {
                // Fallback, wenn nicht genug Platz oder Schrittweite 0 ist
                2
            }
            // Log.d("ZigzagSteps", "Calculated stepsBeforeReverse: $stepsBeforeReverse for width $availableWidth")

            // --- LazyColumn mit dynamisch berechneten Steps ---
            LazyColumn(
                modifier = Modifier
                    // Die Höhe bleibt fix oder relativ zur Screenhöhe
                    .height(percentOfScreenHeight(0.6f))
                    // Füllt die Breite, die BoxWithConstraints vorgibt
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp) // Kein globales Padding
            ) {

                // Konstanten für Zickzack mit *berechnetem* stepsBeforeReverse
                val actualSteps = stepsBeforeReverse // Ist schon >= 2
                val maxIndex = actualSteps - 1
                val cycleLength = if (maxIndex <= 0) 0 else 2 * maxIndex

                item {                             // Dummy‑Zeile nur zum Abstand
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)      // oder ein anderer Wert
                    )
                }

                // itemsIndexed mit der gleichen Zickzack-Logik wie vorher
                itemsIndexed(list) { index, fachName ->
                    val offsetMultiplier = if (cycleLength <= 0) {
                        0
                    } else {
                        val positionInCycle = index % cycleLength
                        if (positionInCycle <= maxIndex) {
                            positionInCycle
                        } else {
                            cycleLength - positionInCycle
                        }
                    }
                    val itemOffset = offsetMultiplier * horizontalOffsetStep
                    println(fachName.id)
                    SimpleListItemStaggered(
                        modifier = Modifier.padding(start = itemOffset),
                        chapter = fachName,
                        {chapter -> selectedChapter = chapter}
                    )
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = itemOffset).padding(horizontal = 16.dp)
                    )
                }
            } // Ende LazyColumn
        } // Ende BoxWithConstraints lambda
        Image(
            painter = painterResource(id = R.drawable.randbackground),
            contentDescription = null, // Rein dekorativ
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize() // Lässt das Bild die Größe der Box annehmen
        )
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Füllt die gesamte Breite
                    .padding(horizontal = 8.dp, vertical = 14.dp)
                    .padding(top = percentOfScreenHeight(0.11f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Linkes Element: Icon-Button
                IconButton(
                    onClick = { changesubject() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.refresh), // Oder dein spezifisches Icon
                        contentDescription = "Aktualisieren", // Für Barrierefreiheit
                        tint = Color.White // Farbe des Icons
                    )
                }

                // 2. Mittleres Element: Text "Fach"
                Text(
                    text = subject,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Color.White, // Textfarbe
                    fontSize = 24.sp, // Schriftgröße (anpassen nach Bedarf)
                    fontFamily = MyFontFamily,
                    fontWeight = FontWeight.Bold, // Fettdruck (optional)
                    textAlign = TextAlign.Center // Text innerhalb seines Bereichs zentrieren
                )

                // 3. Rechtes Element: Button "Quiz"
                Button(
                    onClick = { quiz() },
                    colors = ButtonDefaults.buttonColors(
                        // Hintergrundfarbe des Buttons (dunkler als die Row)
                        containerColor = Color(0xFF0D47A1), // Ein dunkles Blau/Schwarz
                        contentColor = Color.White // Textfarbe im Button
                    ),
                    // Optional: Standard-Padding des Buttons anpassen, falls er zu groß wirkt
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    // Modifier für optionales Padding vom rechten Rand
                    // .padding(start = 8.dp) // Abstand zum mittleren Text
                ) {
                    Text(text = "Quiz")
                }
            }
            Button(
                onClick = { /* Aktion für Klick auf Quiz-Button */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(fraction = 0.7f),
                // --- ECKEN WENIGER RUND MACHEN ---
                // Experimentiere mit dem Wert (z.B. 12.dp, 8.dp, 4.dp)
                shape = RoundedCornerShape(10.dp), // Kleinerer Radius als der Standard

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF51AFEA),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ) // Vertikales Padding evtl. leicht erhöht für bessere Optik bei fester Breite
            ) {
                Text(
                    text = selectedChapter?.let { it.topic+"\n" + it.title }?: " \n ", // KORREKT: Die Bedingung wird direkt ausgewertet
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = percentOfScreenHeight(0.74f))
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            var show by remember { mutableStateOf(false) }
            if (finishedchapterlist != null && selectedChapter != null && nextunlock != null) {
                show =
                    (finishedchapterlist.contains(selectedChapter!!.id) || nextunlock == selectedChapter!!.id)
            } else if (nextunlock != null && selectedChapter != null) {
                show = (nextunlock == selectedChapter!!.id)
            }
            if (show) {
                Button(
                    onClick = { startChapter(selectedChapter!!.id) },
                    modifier = Modifier // 2. Zentriere den Button horizontal
                        .fillMaxWidth(fraction = 0.45f), // Der Button nimmt 80% der Box-Breite ein
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00304F),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        text = "START",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}
private val ItemSize = 96.dp

@Composable
fun SimpleListItemStaggered(
    modifier: Modifier = Modifier,
    chapter: Chapter,
    onChapterClick: (Chapter) -> Unit // NEU: Lambda für Klick-Events
) {
    Image(
        painter = painterResource(R.drawable.content),
        contentDescription = chapter.title, // Besser: Gib eine sinnvolle Beschreibung an
        contentScale = ContentScale.Fit,
        modifier = modifier
            .requiredWidth(ItemSize)
            .padding(vertical = 4.dp)
            .clickable { onChapterClick(chapter) } // NEU: Macht das Bild klickbar
    )
}
@Composable
fun ChangeSubjectContent(
    navController: NavController, // Beibehalten, falls für andere Logik benötigt
    onSubjectSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Äußerster Container für den Hintergrund und die Zentrierung des Dialogs
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Zentriert den Dialog-Inhalt

    ) {
        Image(
            painter = painterResource(id = R.drawable.basiccolor),
            contentDescription = null, // Rein dekorativ
            contentScale = ContentScale.FillBounds, // Streckt das Bild, um die Box zu füllen
            modifier = Modifier.matchParentSize() // Lässt das Bild die Größe der Box annehmen
        )

        // Bild 2: Wird über Bild 1 gelegt und passt sich ein
        Image(
            painter = painterResource(id = R.drawable.backgroundfinal),
            contentDescription = null, // Rein dekorativ
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize() // Lässt das Bild die Größe der Box annehmen
        )
        // Der eigentliche Dialog-Container
        Box(
            modifier = Modifier

                .fillMaxWidth(0.95f) // Nimmt 85% der Bildschirmbreite ein
                .height(percentOfScreenHeight(0.3f))
                //.wrapContentHeight() // Passt die Höhe an den Inhalt an
                .clip(RoundedCornerShape(24.dp)) // Abgerundete Ecken für den Dialog
                .background(Color(0xFF00304F)) // Dunkelblaue Hintergrundfarbe des Dialogs
                .clickable( // Verhindert, dass Klicks auf den Dialog zum Hintergrund durchgehen
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {} // Leerer onClick, um Klicks hier "abzufangen"
                ),
        ) {
            // "X"-Button zum Schließen
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd) // Oben rechts im dunkelblauen Kasten
                    .padding(8.dp) // Kleiner Abstand vom Rand
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp) // Größe des weißen Kreises
                        .clip(CircleShape) // Macht die Box rund
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close, // Standard "Schließen"-Icon
                        contentDescription = "Schließen",
                        tint = Color.Black, // Schwarzes "X"
                        modifier = Modifier.size(20.dp) // Größe des Icons
                    )
                }
            }

            // Spalte für die Auswahlbuttons ("Mathe", "Java")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // Zentriert die Buttons in der Spalte
                verticalArrangement = Arrangement.spacedBy(40.dp) // Abstand zwischen den Buttons
            ) {
                // "Mathe"-Button
                Button(
                    onClick = { onSubjectSelected("math") },
                    modifier = Modifier.fillMaxWidth(), // Füllt die Breite der Spalte
                    shape = RoundedCornerShape(8.dp), // Abgerundete Ecken für den Button
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // Weißer Hintergrund
                        contentColor = Color.Black    // Schwarze Schrift
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp) // Innenabstand des Buttons
                ) {
                    Text(
                        text = "Mathe",
                        // fontFamily = MyFontFamily, // Eigene Schriftart, falls vorhanden
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // "Java"-Button
                Button(
                    onClick = { onSubjectSelected("java") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = "Java",
                        // fontFamily = MyFontFamily, // Eigene Schriftart, falls vorhanden
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun quiz(navController: NavController, mainViewModel: MainViewModel)
{
    val kiFeedback by mainViewModel.kiFeedback.collectAsState()
    var showLastText by remember { mutableStateOf(false) }

    loadingBackground()

    mainViewModel.loadQuestions()
    if (!showLastText)
    {
        MultipleChoice({answers -> showLastText = true
            mainViewModel.kiQuizFeedback(answers)
        })
    }
    else
    {
        if(kiFeedback != null)
        {
            FinalText(listOf(kiFeedback.toString()),answers = listOf(
                AnswerOption("Zurück zur Kapitelauswahl", {navController.navigate(Screen.MainScreen.route)                    {
                    popUpTo(Screen.MainScreen.route) {
                        inclusive = true
                    }
                }})
            ),0.8f,16.sp)
        }

    }
}
