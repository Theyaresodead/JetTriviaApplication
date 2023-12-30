package com.example.jettriviaapplication.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettriviaapplication.data.DataOrEception
import com.example.jettriviaapplication.model.QuestionItem
import com.example.jettriviaapplication.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository):ViewModel(){
    val data: MutableState<DataOrEception<ArrayList<QuestionItem>,Boolean,Exception>> = mutableStateOf(
        DataOrEception(null,true,
    Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading=true
            data.value=repository.getAllQuestions()
            if(data.value.data.toString().isNotEmpty()) {
                data.value.loading= false
            }
        }
    }
    fun getTotalQuestionCount():Int{
     return data.value.data?.toMutableList()?.size!!
    }

}