package com.nodes.sunrise.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.nodes.sunrise.db.entity.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao: BaseDao<Entry> {
    @Query("DELETE FROM Entry")
    override fun deleteAll()

    @Query("SELECT * FROM Entry ORDER BY id")
    override fun getAllEntitiesOrderById(): Flow<List<Entry>>

    @Query("SELECT * FROM Entry WHERE id = :id ORDER BY id")
    override fun getEntityById(id: Int): Flow<Entry>
}