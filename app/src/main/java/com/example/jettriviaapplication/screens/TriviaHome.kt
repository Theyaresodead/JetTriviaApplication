package com.example.jettriviaapplication.sc

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettriviaapplication.component.Questions
import com.example.jettriviaapplication.screens.QuestionViewModel

@Composable
fun TriviaHome(viewmodel: QuestionViewModel = hiltViewModel()){
    Questions(viewmodel)
}
