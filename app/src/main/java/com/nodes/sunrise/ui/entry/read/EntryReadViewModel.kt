package com.nodes.sunrise.ui.entry.read

import androidx.lifecycle.LiveData
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel

class EntryReadViewModel(val repository: AppRepository) : BaseViewModel(repository) {
    lateinit var currentEntry: LiveData<Entry>
}