package com.nodes.sunrise

import android.app.Application
import com.nodes.sunrise.db.AppDatabase
import com.nodes.sunrise.db.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application() {

    // By using 'by lazy', database and repository are created when they are needed rather than
    // when the app starts
    private val database by lazy { AppDatabase.getDatabase(this, CoroutineScope(SupervisorJob())) }
    val repository by lazy {
        AppRepository(
            database.entryDao,
            database.challengeDao,
        )
    }
}