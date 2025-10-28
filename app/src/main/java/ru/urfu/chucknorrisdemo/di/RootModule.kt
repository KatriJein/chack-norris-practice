package ru.urfu.chucknorrisdemo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.urfu.chucknorrisdemo.data.api.ChuckApi
import ru.urfu.chucknorrisdemo.data.repository.ChuckRepository
import ru.urfu.chucknorrisdemo.presentation.viewModel.ChuckViewModel

val rootModule = module {
    viewModel { ChuckViewModel(get()) }
    single { ChuckRepository(get(), get(), androidContext()) }
    single {
        getDataStore(androidContext())
    }
    single { get<Retrofit>().create(ChuckApi::class.java) }
}

fun getDataStore(androidContext: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("default")
    }