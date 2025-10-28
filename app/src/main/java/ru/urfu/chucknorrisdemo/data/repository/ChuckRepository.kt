package ru.urfu.chucknorrisdemo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.urfu.chucknorrisdemo.data.api.ChuckApi
import ru.urfu.chucknorrisdemo.data.model.JokeResponse
import ru.urfu.chucknorrisdemo.domain.repository.IChuckRepository
import ru.urfu.chucknorrisdemo.utils.NetworkUtils

class ChuckRepository(
    private val api: ChuckApi,
    private val dataStore: DataStore<Preferences>,
    private val context: Context
) : IChuckRepository {
    override suspend fun getCategories(): List<String> {
        return if (NetworkUtils.isNetworkAvailable(context)) {
            try {
                api.getCategories()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getRandomJokeByCategory(category: String): JokeResponse? {
        return if (NetworkUtils.isNetworkAvailable(context)) {
            try {
                val joke = api.getRandomJokeByCategory(category)
                saveLastJoke(joke)
                joke
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    override suspend fun saveLastJoke(joke: JokeResponse) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("last_joke_id")] = joke.id
            preferences[stringPreferencesKey("last_joke_value")] = joke.value
            preferences[stringPreferencesKey("last_joke_category")] =
                joke.categories.firstOrNull() ?: ""
            preferences[stringPreferencesKey("last_joke_icon_url")] = joke.iconUrl
            preferences[stringPreferencesKey("last_joke_created_at")] = joke.createdAt
            preferences[stringPreferencesKey("last_joke_url")] = joke.url
        }
    }

    override suspend fun getLastJoke(): JokeResponse? {
        return try {
            dataStore.data.map { preferences ->
                val id = preferences[stringPreferencesKey("last_joke_id")] ?: return@map null
                val value = preferences[stringPreferencesKey("last_joke_value")] ?: return@map null
                val category = preferences[stringPreferencesKey("last_joke_category")] ?: ""
                val iconUrl = preferences[stringPreferencesKey("last_joke_icon_url")] ?: ""
                val createdAt = preferences[stringPreferencesKey("last_joke_created_at")] ?: ""
                val url = preferences[stringPreferencesKey("last_joke_url")] ?: ""

                JokeResponse(
                    id = id,
                    value = value,
                    categories = listOf(category),
                    iconUrl = iconUrl,
                    createdAt = createdAt,
                    updatedAt = createdAt,
                    url = url
                )
            }.first()
        } catch (e: Exception) {
            null
        }
    }
}