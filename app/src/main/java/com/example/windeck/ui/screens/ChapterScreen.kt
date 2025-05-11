package com.example.windeck.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.models.AnswerOption
import com.example.windeck.ui.navigation.Screen
import com.example.windeck.ui.viewmodel.ChapterViewModel
import com.example.windeck.ui.widgets.BottomArrowBar
import com.example.windeck.ui.widgets.FinalText
import com.example.windeck.ui.widgets.MultipleChoice


@Composable
fun ChapterScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) { // Die Box muss den ganzen Platz füllen
        val chapterViewModel: ChapterViewModel = viewModel ()
        chapterViewModel.loadChapterContent()
        var finaltext by remember {
            mutableStateOf(false)
        }
        var result by remember {
            mutableStateOf(false)
        }
        var question by remember {
            mutableStateOf(false)
        }
        var index by remember {
            mutableIntStateOf(0)
        }
        var frontShow by remember {
            mutableStateOf(true)
        }
        var backShow by remember {
            mutableStateOf(false)
        }
        var content = chapterViewModel.getChapterContent()

        if(!finaltext)
        {
            if (question)
            {
                frontShow = false
                MultipleChoice({answers ->
                    if(chapterViewModel.checkAnswer(answers))
                    {
                        result = true
                    }
                    else
                    {
                       result = false
                    }
                    finaltext = true})
            }
            else
            {
                ChapterScreenContent(modifier = Modifier.padding(), content[index])
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomArrowBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    showBack    = backShow,    // Boolean, den du kontrollierst
                    showForward = frontShow,
                    onBackClick = {
                        index--
                        frontShow = true
                        question = false
                        if(index == 0) {
                            backShow = false
                        }
                        else{
                            backShow = true
                        }},
                    onForwardClick = {
                        if(!question)
                        {
                            index++
                            backShow = true
                            if (content.size-1 == index)
                            {
                                frontShow = false
                                question = true

                            }
                            else
                            {
                                frontShow = true
                            }
                        }
                    }
                )
            }
        }
        else
        {
            if (!result)
            {
                FinalText(listOf("Du hast leider die falsche Antwort ausgewählt!"),
                    listOf(
                        AnswerOption(
                            "Unterkapitel Neustarten",
                            { navController.navigate(Screen.ChapterScreen.route)
                            {
                                popUpTo(Screen.ChapterScreen.route) {
                                    inclusive = true
                                }
                            }}),
                        AnswerOption(
                            "Zurück zur Kapitelauswahl",
                            {
                                navController.navigate(Screen.MainScreen.route)                            {
                                    popUpTo(Screen.MainScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    )
                )
            }
            else if(result)
            {
                FinalText(listOf("Herzlichen Glückwunsch du hast das Unterkapitel erfolgreich abgeschlossen!"),
                    listOf(
                        AnswerOption(
                            "Zurück zur Kapitelauswahl",
                            { chapterViewModel.saveFinishedChapter()
                                navController.navigate(Screen.MainScreen.route)
                                {
                                    popUpTo(Screen.MainScreen.route) {
                                        inclusive = true
                                    }
                                }}),
                    )
                )
            }

        }
        }

}

@Composable
fun ChapterScreenContent(modifier: Modifier, content : String) {
    Box(modifier = modifier.fillMaxSize()) {

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
        Text(
            text = content,
            modifier = Modifier.align(Alignment.CenterStart)
                .padding(horizontal = 8.dp),
            color = Color.White,
            fontSize = 20.sp
        )

    }
}
