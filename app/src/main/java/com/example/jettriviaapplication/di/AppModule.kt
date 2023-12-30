package com.example.jettriviaapplication.di

import com.example.jettriviaapplication.network.QuestionApi
import com.example.jettriviaapplication.repository.QuestionRepository
import com.example.jettriviaapplication.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuestionRepository(api:QuestionApi)=QuestionRepository(api)


    @Singleton
    @Provides
    fun provideQuestionApi():QuestionApi{
        return Retrofit.Builder()
            .baseUrl(Constants.baseUrl)       // Calling the base url
            .addConverterFactory(GsonConverterFactory.create())// converting the json files into objects for use
            .build()
            .create(QuestionApi::class.java)   // passing the class responsible for conversion to objects
    }


}