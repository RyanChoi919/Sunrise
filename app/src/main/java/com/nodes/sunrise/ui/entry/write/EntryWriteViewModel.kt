package com.nodes.sunrise.ui.entry.write

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EntryWriteViewModel(val repository: AppRepository) : BaseViewModel(repository) {

    var textCount = ObservableField("0")
    var isTitleEnabled = ObservableField(true)
    var currentEntry = ObservableField(Entry(0, LocalDateTime.now(), "", true, ""))

    fun saveEntry() {
        viewModelScope.launch {
            if (isTitleEnabled.get()!!) {
                currentEntry.get()!!.title = ""

            }
            insert(currentEntry.get()!!)
        }
    }

    fun modifyEntry() {
        viewModelScope.launch {
            update(currentEntry.get()!!)
        }
    }
}