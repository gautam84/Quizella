package com.example.quizella.presentation.questions

data class DialogState(
    val state: Boolean = false,
    val text: String = "Do you want to exit the app? Your progress would be lost."
)