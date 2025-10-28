package ru.urfu.chucknorrisdemo.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.urfu.chucknorrisdemo.data.repository.ChuckRepository
import ru.urfu.chucknorrisdemo.presentation.state.ChuckScreenState

class ChuckViewModel(private val repository: ChuckRepository) : ViewModel() {
    private val mutableState = MutableChuckState()
    val viewState = mutableState as ChuckScreenState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = repository.getCategories()
            if (categories.isNotEmpty()) {
                mutableState.categories = categories
                mutableState.joke = "Select a category to get a joke."
            } else {
                mutableState.categories = emptyList()
                mutableState.joke = "No internet connection."
                loadLastJoke()
            }
        }
    }

    fun onCategoryClicked(category: String) {
        mutableState.selectedCategory = category
        loadJokeByCategory(category)
    }

    private fun loadJokeByCategory(category: String) {
        viewModelScope.launch {
            mutableState.joke = "Loading joke..."

            val jokeResponse = repository.getRandomJokeByCategory(category)
            if (jokeResponse != null) {
                mutableState.joke = jokeResponse.value
            } else {
                loadLastJoke()
            }
        }
    }

    private fun loadLastJoke() {
        viewModelScope.launch {
            val lastJoke = repository.getLastJoke()
            if (lastJoke != null) {
                mutableState.joke = lastJoke.value
                mutableState.selectedCategory = lastJoke.categories.firstOrNull() ?: ""
            } else {
                mutableState.joke = "No internet connection and no saved jokes available."
            }
        }
    }

    private class MutableChuckState : ChuckScreenState {
        override var categories: List<String> by mutableStateOf(emptyList())
        override var selectedCategory: String by mutableStateOf("")
        override var joke: String by mutableStateOf("")
    }
}