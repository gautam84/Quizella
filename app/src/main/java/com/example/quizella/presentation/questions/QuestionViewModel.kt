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
        }
    }
}