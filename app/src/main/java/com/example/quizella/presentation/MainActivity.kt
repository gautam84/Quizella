package com.example.quizella.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizella.presentation.common.AppEvent
import com.example.quizella.presentation.common.AppViewModel
import com.example.quizella.presentation.finish.FinishScreen
import com.example.quizella.presentation.main.MainScreen
import com.example.quizella.presentation.questions.QuestionScreen
import com.example.quizella.presentation.util.SetupNavigation
import com.example.quizella.ui.theme.QuizellaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel.onEvent(AppEvent.ClearCache)


        setContent {
            QuizellaTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary
                ) {
                    SetupNavigation()
                }
            }
        }
    }


}
