package com.example.windeck.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.windeck.ui.data.TopUser
import com.example.windeck.ui.viewmodel.RankingViewModel
import com.example.windeck.ui.widgets.SimpleBottomAppBar
import com.example.windeck.ui.widgets.percentOfScreenHeight

@Composable
fun RankingScreen(navController: NavController) {
    val rankingViewModel: RankingViewModel = viewModel()
    val response by rankingViewModel.response.collectAsState()

    LaunchedEffect(Unit) {
        rankingViewModel.getAllUserAchievements()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        response?.let {
            RankingScreenContent(modifier = Modifier.padding(),
                it
            )
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SimpleBottomAppBar(navController = navController)
        }
    }
}
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RankingScreenContent(modifier: Modifier, list: List<TopUser>) {
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
                .padding(top = percentOfScreenHeight(0.23f))
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(percentOfScreenHeight(0.61f))
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {

                itemsIndexed(list) { index, fachName ->

                    RankingEntry(index +1, fachName.username, fachName.total_points)
                    Spacer(modifier= Modifier.padding(vertical = 10.dp))
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
                    .padding(top = percentOfScreenHeight(0.1f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RANKING",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Color.White,
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun RankingEntry(
    rank: Int,
    name: String,
    points: Int,
    modifier: Modifier = Modifier
) {
    val entryShape = RoundedCornerShape(8.dp)
    val entryBackgroundColor = Color(0xFF00304F)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = entryBackgroundColor, shape = entryShape)
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rank.",
                color = Color.White,
                fontSize = 20.sp
            )
            Text(
                text = name,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = points.toString(),
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}