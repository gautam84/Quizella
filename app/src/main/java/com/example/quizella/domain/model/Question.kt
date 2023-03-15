package com.example.quizella.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions_table")
data class Question(
    val category: String,
    @PrimaryKey
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
