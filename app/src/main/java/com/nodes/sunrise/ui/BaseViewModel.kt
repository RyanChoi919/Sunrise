package com.nodes.sunrise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nodes.sunrise.db.AppRepository
import java.time.LocalDate

open class BaseViewModel(private val repository: AppRepository) : ViewModel() {

    val allEntries = repository.allEntries.asLiveData()

    suspend fun <T> insert(t: T) {
        repository.insert(t)
    }

    suspend fun <T> update(t: T) {
        repository.update(t)
    }

    suspend fun <T> delete(t: T) {
        repository.delete(t)
    }
}