package com.nodes.sunrise.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.nodes.sunrise.db.entity.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao: BaseDao<Entry> {
    @Query("DELETE FROM Entry")
    fun deleteAll()

    @Query("SELECT * FROM Entry ORDER BY dateTime DESC")
    fun getAllEntriesOrderByDateDesc(): Flow<List<Entry>>

    @Query("SELECT * FROM Entry WHERE id = :id ORDER BY id")
    fun getEntryById(id: Int): Flow<Entry>
}