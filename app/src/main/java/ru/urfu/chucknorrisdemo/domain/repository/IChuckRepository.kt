package ru.urfu.chucknorrisdemo.domain.repository

import ru.urfu.chucknorrisdemo.data.model.JokeResponse


interface IChuckRepository {
    suspend fun getCategories(): List<String>
    suspend fun getRandomJokeByCategory(category: String): JokeResponse?
    suspend fun saveLastJoke(joke: JokeResponse)
    suspend fun getLastJoke(): JokeResponse?
}