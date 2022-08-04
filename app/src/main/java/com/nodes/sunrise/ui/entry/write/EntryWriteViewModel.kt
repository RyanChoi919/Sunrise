package com.nodes.sunrise.ui.entry.write

import androidx.lifecycle.viewModelScope
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EntryWriteViewModel(val repository: AppRepository) : BaseViewModel(repository) {

    lateinit var currentEntry: Entry

    fun saveEntry() {
        viewModelScope.launch {
            insert(currentEntry)
        }
    }

    fun modifyEntry() {
        viewModelScope.launch {
            update(currentEntry)
        }
    }

}