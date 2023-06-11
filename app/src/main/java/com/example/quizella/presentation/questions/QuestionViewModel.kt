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

package com.example.quizella.presentation.questions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.quizella.domain.model.Question
import com.example.quizella.domain.repository.QuestionsRepository
import com.example.quizella.domain.repository.ScoreRepository
import com.example.quizella.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _currQuestionState = mutableStateOf(QuestionState())
    val currQuestionState: State<QuestionState> = _currQuestionState

    private val _questionsList = mutableStateOf(QuestionListState())
    val questionsList: State<QuestionListState> = _questionsList


    init {

        viewModelScope.launch {
            questionsRepository.getQuestions().collect {
                _questionsList.value = questionsList.value.copy(
                    list = it.toMutableList()
                )
                _currQuestionState.value = currQuestionState.value.copy(
                    questionNo = 1,
                    currQuestion = it[0]
                )
            }
            scoreRepository.getPoints.collect {
                _questionsList.value = questionsList.value.copy(
                    points = it,
                    tempPoints = 0
                )
            }
        }
    }

    private val _dialogState = mutableStateOf(DialogState())
    val dialogState: State<DialogState> = _dialogState


    fun onEvent(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.NextQuestion -> {

                if (_currQuestionState.value.isEnabled) {
                    _currQuestionState.value = currQuestionState.value.copy(
                        isEnabled = false
                    )
                } else {
                    if (_currQuestionState.value.currQuestion?.correctAnswer == _currQuestionState.value.currAns) {

                        _currQuestionState.value = currQuestionState.value.copy(
                            questionNo = _currQuestionState.value.questionNo!! + 1,
                            currQuestion = _questionsList.value.list[_currQuestionState.value.questionNo!!]
                        )
                        _currQuestionState.value = currQuestionState.value.copy(
                            isEnabled = true,
                            currAns = ""
                        )
                        if(_currQuestionState.value.questionNo == 21){
                            event.navHostController.navigate(Screen.FinishScreen.route) {
                                popUpTo(event.navHostController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                            viewModelScope.launch {
                                _questionsList.value = questionsList.value.copy(
                                    tempPoints = _currQuestionState.value.questionNo!! - 1
                                )
                                viewModelScope.launch {
                                    scoreRepository.setTempPoints(_currQuestionState.value.questionNo!! - 1)
                                    scoreRepository.getTempPoints.collect { temporaryPoints ->
                                        scoreRepository.getPoints.collect { points ->
                                            if (points < temporaryPoints) {
                                                scoreRepository.setPoints(temporaryPoints)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        event.navHostController.navigate(Screen.FinishScreen.route) {
                            popUpTo(event.navHostController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                        viewModelScope.launch {
                            _questionsList.value = questionsList.value.copy(
                                tempPoints = _currQuestionState.value.questionNo!! - 1
                            )
                            viewModelScope.launch {
                                scoreRepository.setTempPoints(_currQuestionState.value.questionNo!! - 1)
                                scoreRepository.getTempPoints.collect { temporaryPoints ->
                                    scoreRepository.getPoints.collect { points ->
                                        if (points < temporaryPoints) {
                                            scoreRepository.setPoints(temporaryPoints)
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            is QuestionEvent.ChangeAnswer -> {
                _currQuestionState.value = currQuestionState.value.copy(
                    currAns = event.ans
                )
            }

            is QuestionEvent.OpenDialog -> {
                _dialogState.value = dialogState.value.copy(
                    state = true
                )
            }
            is QuestionEvent.CloseDialog -> {
                _dialogState.value = dialogState.value.copy(
                    state = false
                )
            }
        }
    }
}