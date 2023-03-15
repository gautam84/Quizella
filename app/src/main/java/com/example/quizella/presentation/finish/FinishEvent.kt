package com.example.quizella.presentation.finish

import android.content.Context
import androidx.navigation.NavHostController

sealed class FinishEvent {
    data class PlayAgain(val navHostController: NavHostController) : FinishEvent()
    data class Share(val context: Context) : FinishEvent()
    data class GoHome(val navHostController: NavHostController) : FinishEvent()

}