package com.nodes.sunrise.ui.entry.write

import android.location.Location
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.db.entity.EntryFactory
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch

class EntryWriteViewModel(val repository: AppRepository) : BaseViewModel(repository) {

    var textCount = ObservableField("0")
    var currentEntry = ObservableField(EntryFactory.create())
    lateinit var prevEntry: Entry
    var isPrevEntrySet = false

    fun saveEntry() {
        viewModelScope.launch {
            if (!currentEntry.get()!!.isTitleEnabled) {
                currentEntry.get()!!.title = ""
            }

            if (currentEntry.get()!!.title == "") {
                currentEntry.get()!!.isTitleEnabled = false
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

        if (!isPrevEntrySet) {
            prevEntry = entry.copy()
            isPrevEntrySet = true
        }

        Log.d(
            TAG,
            "updateEntryLocation: latitude = ${entry.latitude}, longitude = ${entry.longitude}"
        )
    }

    fun removeEntryLocation() {
        val entry = currentEntry.get()!!.apply {
            latitude = null
            longitude = null
        }

        currentEntry.set(entry)

        Log.d(
            TAG,
            "removeEntryLocation: latitude = ${entry.latitude}, longitude = ${entry.longitude}"
        )
    }

    fun isEntryModified(): Boolean {
        return currentEntry.get()!! != prevEntry
    }
}