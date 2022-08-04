package com.nodes.sunrise.ui.entry.write

import androidx.lifecycle.viewModelScope
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EntryWriteViewModel(val repository: AppRepository) : BaseViewModel(repository) {

    var dateTime: LocalDateTime = LocalDateTime.now()
    lateinit var entry: Entry

    /**
     * 새로운 entry를 작성한 후 데이터를 저장하는 메소드
     */
    fun write(title: String, content: String) {
        entry = Entry(0, dateTime, title, true, content)

        viewModelScope.launch {
            insert(entry)
        }
    }

    /**
     * 기존 entry를 수정한 후 수정된 데이터를 저장하는 메소드
     */
    fun modify(title: String, content: String) {
        entry.title = title
        entry.content = content

        viewModelScope.launch {
            insert(entry)
        }
    }

}