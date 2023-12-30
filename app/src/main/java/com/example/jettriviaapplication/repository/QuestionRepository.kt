package com.example.jettriviaapplication.repository

import com.example.jettriviaapplication.data.DataOrEception
import com.example.jettriviaapplication.model.QuestionItem
import com.example.jettriviaapplication.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

    private val dataOrEception =DataOrEception<ArrayList<QuestionItem>,Boolean,Exception>()
    suspend fun getAllQuestions() : DataOrEception<ArrayList<QuestionItem>,Boolean,Exception>{
        try {
                dataOrEception.loading=true
            dataOrEception.data=api.getAllQuestions()
            if(dataOrEception.data.toString().isNotEmpty()) dataOrEception.loading=false
        }
        catch (exception:Exception){
           dataOrEception.e=exception
        }
        return dataOrEception
    }

}