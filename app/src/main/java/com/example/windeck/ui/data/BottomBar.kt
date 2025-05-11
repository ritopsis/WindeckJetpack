package com.example.windeck.ui.data

import com.example.windeck.R
import com.example.windeck.ui.navigation.Screen

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home: BottomBar(
        route = Screen.MainScreen.route,
        title = "Home",
        icon =  R.drawable.home
    )
    object Ranking : BottomBar(
        route = Screen.RankingScreen.route,
        title = "Ranking",
        icon = R.drawable.ranking
    )
    object Profile: BottomBar(
        route = Screen.ProfileScreen.route,
        title = "Profile",
        icon = R.drawable.profile
    )

}