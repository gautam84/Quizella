package com.example.quizella.presentation.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizella.domain.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : ViewModel() {


    private val _points = mutableStateOf(0)
    val points: State<Int> = _points

    init {
      viewModelScope.launch  {
            scoreRepository.getPoints.collect {
                _points.value = it
            }
        }
    }


}