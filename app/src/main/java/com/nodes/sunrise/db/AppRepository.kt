package com.nodes.sunrise.db

import androidx.annotation.WorkerThread
import com.nodes.sunrise.db.dao.ChallengeDao
import com.nodes.sunrise.db.dao.ChallengeGroupDao
import com.nodes.sunrise.db.dao.EntryDao
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(
    val entryDao: EntryDao,
    val challengeDao: ChallengeDao,
    val challengeGroupDao: ChallengeGroupDao
) {

    val allEntries = entryDao.getAllEntriesOrderById()
    val allChallenges = challengeDao.getAllChallengesOrderById()
    val allChallengeGroupsWithChallenges = challengeGroupDao.getChallengeGroupsWithChallenges()

    @WorkerThread
    suspend fun <T> insert(t: T) = withContext(Dispatchers.IO) {
        when (t) {
            is Entry -> {
                entryDao.insert(t)
            }
            is Challenge -> {
                challengeDao.insert(t)
            }
        }
    }

    @WorkerThread
    suspend fun <T> update(t: T) = withContext(Dispatchers.IO) {
        when (t) {
            is Entry -> {
                entryDao.update(t)
            }
            is Challenge -> {
                challengeDao.update(t)
            }
        }
    }

    @WorkerThread
    suspend fun <T> delete(t: T) = withContext(Dispatchers.IO) {
        when (t) {
            is Entry -> {
                entryDao.delete(t)
            }
            is Challenge -> {
                challengeDao.delete(t)
            }
        }
    }
}