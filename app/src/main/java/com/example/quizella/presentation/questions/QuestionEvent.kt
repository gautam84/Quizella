package com.example.quizella.presentation.questions

import androidx.navigation.NavHostController

sealed class QuestionEvent {
    data class NextQuestion(val navHostController: NavHostController) : QuestionEvent()
    data class ChangeAnswer(val ans: String) : QuestionEvent()


}