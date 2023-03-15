package com.example.quizella.presentation.questions

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.quizella.R
import com.example.quizella.domain.model.Question

@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = hiltViewModel(),
    navHostController: NavHostController

) {
    val currQuestion by viewModel.currQuestionState
    val context = LocalContext.current




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = stringResource(R.string.exit_app)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (currQuestion.currQuestion?.question != "") {
                QuestionCard(
                    currQuestion.questionNo!!,
                    Question(
                        category = currQuestion.currQuestion?.category!!,
                        id = currQuestion.currQuestion?.id!!,
                        correctAnswer = currQuestion.currQuestion?.correctAnswer!!,
                        incorrectAnswers = currQuestion.currQuestion?.incorrectAnswers!!,
                        tags = currQuestion.currQuestion?.tags!!,
                        question = currQuestion.currQuestion?.question!!,
                        type = currQuestion.currQuestion?.type!!,
                        difficulty = currQuestion.currQuestion?.difficulty!!,
                        regions = null,
                        isNiche = currQuestion.currQuestion?.isNiche!!
                    ),
                    currQuestion.isEnabled,
                    currQuestion.currAns,
                    onClick = {
                        viewModel.onEvent(QuestionEvent.ChangeAnswer(it))
                    })
                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = {
                        if (currQuestion.currAns != "") {
                            viewModel.onEvent(
                                QuestionEvent.NextQuestion(
                                    navHostController,
                                )
                            )
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.please_select_an_answer),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFAF0171).copy(
                            0.7f
                        )
                    )
                ) {
                    Text(
                        text = if (currQuestion.isEnabled) {
                            stringResource(R.string.submit)
                        } else {
                            stringResource(R.string.next)
                        }
                    )

                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun QuestionCard(
    questionNo: Int,
    question: Question,
    isEnabled: Boolean,
    currAns: String,
    onClick: (String) -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE80F88).copy(0.75f))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Q$questionNo. " + question.question, fontSize = 16.sp)
        }
        LazyColumn(
            contentPadding = PaddingValues(0.dp, 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val answers = question.incorrectAnswers + question.correctAnswer
            items(answers) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            BorderStroke(
                                if (!isEnabled && (it == question.correctAnswer)) {
                                    3.dp
                                } else {
                                    0.dp
                                }, Color(0xFF058011)
                            ), RoundedCornerShape(16.dp)
                        )
                        .background(
                            if (!isEnabled && (it == question.correctAnswer)) {
                                Color(0xFF1AC536)
                            } else {
                                Color(0xFFAF0171)

                            }
                        )
                        .clickable(enabled = isEnabled) {
                            onClick(it)
                        }
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it)
                        RadioButton(
                            selected = currAns == it,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (!isEnabled) {
                                    Color.Black
                                } else {
                                    Color.Cyan
                                }
                            )
                        )
                    }
                }
            }
        }

    }
}
