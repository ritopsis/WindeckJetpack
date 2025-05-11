package com.example.windeck.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.sharp.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.windeck.R
import com.example.windeck.ui.navigation.Screen
import com.example.windeck.ui.theme.MyFontFamily
import com.example.windeck.ui.viewmodel.AuthViewModel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

@Composable
fun AuthForm(navController: NavController, register: Boolean = false, authViewModel: AuthViewModel) {
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var pwVisible by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember { mutableStateOf("") }
    val response by authViewModel.response.collectAsState()
    val checkServer by authViewModel.serverAvailability.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.basiccolor),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(R.drawable.loginbackground),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = percentOfScreenHeight(0.12f))
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
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement  = Arrangement.spacedBy(
                28.dp,
                Alignment.CenterVertically
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .padding(top = percentOfScreenHeight(0.24f))
        ) {
            Text(errorMessage,
                fontSize = 20.sp,
                textAlign = TextAlign.Center)

            OutlinedTextField(
                value           = username,
                onValueChange   = { username = it },
                singleLine      = true,
                placeholder = {
                    Text(
                        "BENUTZERNAME",
                        color     = Color.White,
                        fontSize = 20.sp,
                    )
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, color = Color.White, fontFamily = MyFontFamily,
                    fontWeight = FontWeight.Bold),
                modifier        = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = Color.White,
                    unfocusedBorderColor    = Color.White,
                    disabledBorderColor     = Color.White.copy(alpha = .4f),
                    focusedContainerColor   = Color(0xFF65BCF0),
                    unfocusedContainerColor = Color(0xFF65BCF0),
                    disabledContainerColor  = Color(0xFF65BCF0),
                    cursorColor             = Color.White
                ),
            )

            OutlinedTextField(
                value           = password,
                onValueChange   = { password = it },
                singleLine      = true,
                placeholder = {
                    Text(
                        "PASSWORT",
                        color     = Color.White,
                        fontSize = 20.sp,
                    )
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, color = Color.White, fontFamily = MyFontFamily,
                    fontWeight = FontWeight.Bold),
                visualTransformation =
                    if (pwVisible) VisualTransformation.None
                    else           PasswordVisualTransformation(),
                trailingIcon    = {
                    val icon      =
                        if (pwVisible) Icons.Default.Visibility
                        else           Icons.Sharp.VisibilityOff
                    val desc       =
                        if (pwVisible) "Passwort verbergen"
                        else           "Passwort anzeigen"
                    IconButton(onClick = { pwVisible = !pwVisible }) {
                        Icon(icon, contentDescription = desc)
                    }
                },
                modifier        = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = Color.White,
                    unfocusedBorderColor    = Color.White,
                    disabledBorderColor     = Color.White.copy(alpha = .4f),

                    focusedContainerColor   = Color(0xFF65BCF0),
                    unfocusedContainerColor = Color(0xFF65BCF0),
                    disabledContainerColor  = Color(0xFF65BCF0),

                    cursorColor             = Color.White
                ),
            )

            Button(
                    onClick = { if(checkUsername(username) && checkPassword(password))
                    {
                        if(register) authViewModel.register(username,password) else authViewModel.login(username,password)
                    }
                        else
                    {
                        errorMessage = "Benutzername: mind. 4 Zeichen, keine Sonderzeichen. Passwort: mind. 6 Zeichen, 1 Zahl."
                    }
                        },
                colors  = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor   = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(110.dp)
                    .paint(
                        painterResource(R.drawable.buttonblau),
                        contentScale = ContentScale.FillBounds
                    ),
            ) {
                Text(if (register) "REGISTRIEREN" else "ANMELDEN", fontSize = 20.sp, modifier = Modifier.padding(bottom = 6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { if (register) navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    } else navController.navigate(Screen.RegistrationScreen.route){
                        popUpTo(Screen.RegistrationScreen.route) {
                            inclusive = true
                        }
                    }},
                    colors  = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor   = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(100.dp)
                        .paint(
                            painterResource(R.drawable.buttondark),
                            contentScale = ContentScale.FillBounds
                        ),
                ) {
                    Text(if (register) "ANMELDEN" else "REGISTRIEREN", fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(bottom = 6.dp))
                }
            }
        }
    }
    LaunchedEffect(response) {
        response?.let {
            when (it.status) {
                HttpStatusCode.OK -> {
                    authViewModel.clearResponse()
                    authViewModel.clearServer()
                    if(response!!.bodyAsText().contains("1") || register)
                    {
                        //navController.navigate(Screen.WelcomeScreen.route)
                        navController.navigate(Screen.WelcomeScreen.route) {
                            popUpTo(Screen.StartScreen.route) { // removes all screens from backstack inclusive startscreen
                                inclusive = true
                            }
                        }
                    } else {
                        //navController.navigate(Screen.MainScreen.route)
                        navController.navigate(Screen.MainScreen.route) {
                            popUpTo(Screen.StartScreen.route) { // removes all screens from backstack inclusive startscreen
                                inclusive = true
                            }
                        }
                    }
                }
                else -> {
                    errorMessage = if(it.status.description == "Conflict") {
                        "Benutzername bereits vergeben."
                    } else{
                        "Ungültiger Benutzername oder Passwort."
                    }
                }
            }
        }
    }
    LaunchedEffect(checkServer) {
        checkServer?.let { isAvailable ->
            if(!isAvailable)
            {
                errorMessage =  "Server nicht verfügbar."
            }
        }
    }
}

@Composable
fun percentOfScreenHeight(percent: Float): Dp {
    val config = LocalConfiguration.current
    return (config.screenHeightDp * percent).dp
}
fun checkUsername(name: String): Boolean {
    val usernameRegex = Regex("^[a-zA-Z0-9]+$")
    return name.length > 3 && usernameRegex.matches(name)

}
fun checkPassword(password: String): Boolean {
    val digitRegex = Regex("\\d")
    return password.length > 5 && digitRegex.containsMatchIn(password)

}