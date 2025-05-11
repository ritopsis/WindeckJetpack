package com.example.windeck.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.data.Achievement
import com.example.windeck.ui.viewmodel.ProfileViewModel
import com.example.windeck.ui.widgets.SimpleBottomAppBar
import com.example.windeck.ui.widgets.percentOfScreenHeight

@Composable
fun ProfileScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val response by profileViewModel.response.collectAsState()
    var username by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(Unit) {
        profileViewModel.getAchievements()
        username = profileViewModel.getUsername()
    }

    Box(modifier = Modifier.fillMaxSize()) {

            ProfileScreenContent(modifier = Modifier.padding(), username,response
            )
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SimpleBottomAppBar(navController = navController)
        }
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileScreenContent(modifier: Modifier, name: String, list: List<Achievement>?) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = percentOfScreenHeight(0.28f))
                .padding(horizontal = 2.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .height(percentOfScreenHeight(0.6f))
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {
                if(list != null)
                {
                    itemsIndexed(list) { index, fachName ->

                        AchievementsEntry(fachName.name, fachName.description, fachName.points)
                        Spacer(modifier= Modifier.padding(vertical = 10.dp))
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.randbackground),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 14.dp)
                    .padding(top = percentOfScreenHeight(0.12f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
            // Damit das Padding vom Namen korrekt ist
            Button(
                onClick = { /* Aktion für Klick auf Quiz-Button */ },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(fraction = 0.7f)
                    .padding(top = percentOfScreenHeight(0.02f)),
                shape = RoundedCornerShape(10.dp), // Kleinerer Radius als der Standard

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF51AFEA),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(
                    horizontal = 18.dp,
                    vertical = 12.dp
                )
            ) {
                Text(
                    text = "Achievements",
                    textAlign = TextAlign.Center,
                    fontSize =26.sp,
                )
            }
        }
    }
}


@Composable
fun AchievementsEntry(
    title: String,
    description: String,
    points: Int,
    modifier: Modifier = Modifier
) {
    val entryShape = RoundedCornerShape(12.dp)
    val entryBackgroundColor = Color(0xFF00304F)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = entryBackgroundColor, shape = entryShape)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Punkte Text: Am rechten Rand, vertikal mittig
                Text(
                    text = "+$points",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
