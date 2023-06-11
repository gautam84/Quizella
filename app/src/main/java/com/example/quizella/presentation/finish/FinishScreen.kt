/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

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
