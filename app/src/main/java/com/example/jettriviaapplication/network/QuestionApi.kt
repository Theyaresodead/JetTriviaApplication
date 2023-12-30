package com.example.jettriviaapplication.network

import com.example.jettriviaapplication.data.DataOrEception
import com.example.jettriviaapplication.model.Question
import com.example.jettriviaapplication.model.QuestionItem
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface QuestionApi {
    @GET("world.json") // this get function will concatenate this with the url in constants object to get this link of the api.
    suspend fun getAllQuestions():Question

}