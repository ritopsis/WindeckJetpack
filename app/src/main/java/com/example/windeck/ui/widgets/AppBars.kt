package com.example.windeck.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.windeck.R
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.windeck.ui.data.BottomBar
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding



@Composable
fun SimpleBottomAppBar(navController: NavController) {
    val screens = listOf(
        BottomBar.Home,
        BottomBar.Ranking,
        BottomBar.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF00304F),
    ) {
        screens.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(item.route) {
                            inclusive = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = Color.Unspecified
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                indicatorColor      = Color.Black.copy(alpha = 0.2f)
            )
            )
        }
    }
}
@Composable
fun BottomArrowBar(
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    showForward: Boolean = true,
    onBackClick: () -> Unit = {},
    onForwardClick: () -> Unit = {}
) {
    val barHeight = 80.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(Color(0xFF00304F))
            .padding(WindowInsets.navigationBars
                .asPaddingValues()) // Abstand zum System‑Nav‑Bar
    ) {
        if (showBack) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp)
            ) {
                Icon(
                    painterResource(R.drawable.backarrow),
                    contentDescription = "Zurück",
                    tint = Color.Unspecified
                )
            }
        }
        if (showForward) {
            IconButton(
                onClick = onForwardClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
            ) {
                Icon(
                    painterResource(R.drawable.frontarrow),
                    contentDescription = "Weiter",
                    tint = Color.Unspecified
                )
            }
        }
    }
}