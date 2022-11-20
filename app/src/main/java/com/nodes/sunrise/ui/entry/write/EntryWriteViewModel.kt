package com.nodes.sunrise.ui.entry.write

import android.location.Address
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class EntryWriteViewModel(val repository: AppRepository) : BaseViewModel(repository) {

    companion object {
        private const val TAG = "EntryWriteViewModel.TAG"
    }

    var textCount = ObservableField("0")
    var currentEntry = ObservableField(Entry())
    lateinit var prevEntry: Entry
    var isPrevEntrySet = false

    fun saveEntry() {
        viewModelScope.launch {
            syncTitleAndTitleEnabled()
            insert(currentEntry.get()!!)
        }
    }

    fun modifyEntry() {
        viewModelScope.launch {
            syncTitleAndTitleEnabled()
            update(currentEntry.get()!!)
        }
    }

    fun updateEntryLocation(location: Location, address: Address) {
        val entry = currentEntry.get()!!.apply {
            latitude = location.latitude
            longitude = location.longitude
            this.address = address
        }

        currentEntry.set(entry)

        if (!isPrevEntrySet) {
            prevEntry = entry.copy()
            isPrevEntrySet = true
        }

        Log.d(
            TAG,
            "updateEntryLocation: latitude = ${entry.latitude}, longitude = ${entry.longitude}, address = ${entry.address}"
        )
    }

    fun removeEntryLocation() {
        val entry = currentEntry.get()!!.apply {
            latitude = null
            longitude = null
            this.address = null
        }

        currentEntry.set(entry)

        Log.d(
            TAG,
            "removeEntryLocation: latitude = ${entry.latitude}, longitude = ${entry.longitude}, address = ${entry.address}"
        )
    }

    fun isEntryModified(): Boolean {
        return currentEntry.get()!! != prevEntry
    }

    fun setEntryTitleEnabled(shouldEnable: Boolean) {
        val entry = currentEntry.get()!!.apply {
            isTitleEnabled = shouldEnable
        }
        currentEntry.set(entry)
    }

    fun setEntryDate(date: LocalDate) {
        val updatedEntry = currentEntry.get()!!.apply {
            dateTime = LocalDateTime.from(dateTime).withYear(date.year).withMonth(date.monthValue)
                .withDayOfMonth(date.dayOfMonth)
        }
        currentEntry.set(updatedEntry)
    }

    fun setEntryTime(time: LocalTime) {
        val updatedEntry = currentEntry.get()!!.apply {
            dateTime = LocalDateTime.from(dateTime).withHour(time.hour).withMinute(time.minute)
        }
        currentEntry.set(updatedEntry)
    }

    fun setPhotoUriList(uriList: List<Uri>) {
        val updatedEntry = currentEntry.get()!!.apply {
            photos = uriList
        }
        currentEntry.set(updatedEntry)
    }

    private fun syncTitleAndTitleEnabled() {
        val entry = currentEntry.get()!!

        if (entry.isTitleEnabled) {
            /* 타이틀이 활성화되어 있더라도, 타이틀 내용이 비어있는 경우 타이틀 비활성화 */
            entry.isTitleEnabled = entry.title != ""
        } else {
            /* 타이틀이 비활성화된 경우, 타이틀 내용을 삭제 */
            entry.title = ""
        }

        currentEntry.set(entry)
    }
}