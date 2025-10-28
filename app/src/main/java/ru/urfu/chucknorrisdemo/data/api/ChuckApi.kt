package ru.urfu.chucknorrisdemo.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.urfu.chucknorrisdemo.data.model.JokeResponse

interface ChuckApi {
    @GET("jokes/categories")
    suspend fun getCategories(): List<String>

    @GET("jokes/random")
    suspend fun getRandomJokeByCategory(@Query("category") category: String): JokeResponse
}