package com.nodes.sunrise.ui.entry.write

import android.location.Location
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.db.entity.EntryFactory
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.log

class EntryWriteViewModel(val repository: AppRepository) : BaseViewModel(repository) {

    var textCount = ObservableField("0")
    var currentEntry = ObservableField(EntryFactory.create())

    fun saveEntry() {
        viewModelScope.launch {
            if (currentEntry.get()!!.isTitleEnabled) {
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

    fun updateEntryLocation(location: Location) {
        val entry = currentEntry.get()!!.apply {
            latitude = location.latitude
            longitude = location.longitude
        }

        currentEntry.set(entry)
    }

}