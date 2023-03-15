package com.example.quizella.presentation.finish

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.quizella.R
import com.example.quizella.presentation.main.MainViewModel

@Composable
fun FinishScreen(
    viewModel: FinishViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(stringResource(R.string.game_over), fontSize = 42.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.score),
            color = Color.White.copy(0.7f)
        )
        Text(
            text = viewModel.points.value.toString(),
            fontSize = 88.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Column(modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    viewModel.onEvent(FinishEvent.PlayAgain(navHostController))
                }
                .background(Color(0xFF790252).copy(0.5f))
                .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Sync, contentDescription = stringResource(R.string.restart))
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        viewModel.onEvent(FinishEvent.Share(context))
                    }
                    .background(Color(0xFF790252).copy(0.5f))
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = stringResource(R.string.share)
                )

            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        viewModel.onEvent(FinishEvent.GoHome(navHostController))
                    }
                    .background(Color(0xFF790252).copy(0.5f))
                    .padding(16.dp)
            ) {

                Icon(imageVector = Icons.Outlined.Home, contentDescription = stringResource(R.string.home))

            }
        }
    }
}
