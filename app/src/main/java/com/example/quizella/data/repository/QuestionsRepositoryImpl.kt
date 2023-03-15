package com.example.quizella.data.repository

import com.example.quizella.data.data_source.local.QuestionCacheDao
import com.example.quizella.domain.model.Question
import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.domain.repository.QuestionsRepository
import kotlinx.coroutines.flow.Flow

class QuestionsRepositoryImpl(
    private val dao: QuestionCacheDao,
    private val triviaService: TriviaService
) : QuestionsRepository {

    override suspend fun getQuestions(): Flow<List<Question>> {
        triviaService.getQuestions().forEach {
            dao.insertQuestion(
                Question(
                    id = it.id,
                    category = it.category,
                    correctAnswer = it.correctAnswer,
                    incorrectAnswers = it.incorrectAnswers,
                    question = it.question,
                    tags = it.tags,
                    type = it.type,
                    difficulty = it.difficulty,
                    regions = it.regions,
                    isNiche = it.isNiche
                )
            )

        }
        return dao.getQuestions()
    }


    override suspend fun clearQuestions() {
        dao.clear()
    }
}