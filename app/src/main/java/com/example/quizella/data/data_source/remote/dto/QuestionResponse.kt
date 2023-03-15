package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val category: String,
    val id: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val tags: List<String>,
    val question: String,
    val type: String,
    val difficulty: String,
    val regions: List<String>?,
    val isNiche: Boolean
    )
