package com.example.windeck.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.navigation.Screen
import com.example.windeck.ui.widgets.percentOfScreenHeight


@Composable
fun StartScreen(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.basiccolor),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(R.drawable.backgroundfinal),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = percentOfScreenHeight(0.22f))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.windeckconnectlogo),
                contentDescription = "Windeck Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(110.dp)
            )
            Text(
                text = "Erweitere dein Wissen!",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                0.dp,
                Alignment.CenterVertically
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 150.dp)
        ) {
            Button(
                onClick = { navController.navigate(Screen.LoginScreen.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(110.dp)
                    .paint(
                        painterResource(R.drawable.buttonverlauf),
                        contentScale = ContentScale.FillBounds    // skaliert das Bild flächendeckend
                    ),
            ) {
                Text(
                    text = "ANMELDEN",
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            Button(
                onClick = { navController.navigate(Screen.RegistrationScreen.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(110.dp)
                    .paint(
                        painterResource(R.drawable.buttondark),
                        contentScale = ContentScale.FillBounds
                    ),
            ) {
                Text(
                    text = "REGISTRIEREN",
                    fontSize = 22.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}

