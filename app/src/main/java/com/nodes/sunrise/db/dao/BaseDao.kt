package com.nodes.sunrise.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {

    @Insert
    fun insert(t: T)

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)

    fun deleteAll()

    fun getAllEntitiesOrderById(): Flow<List<T>>

    fun getEntityById(id: Int): Flow<T>
}