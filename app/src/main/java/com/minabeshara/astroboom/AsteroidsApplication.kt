package com.minabeshara.astroboom

import android.app.Application
import androidx.work.*
import com.minabeshara.astroboom.work.AsteroidsDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidsApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            fetchAsteroidsDataDaily()
        }
    }

    private fun fetchAsteroidsDataDaily(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.METERED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<AsteroidsDataWorker>(1,TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            AsteroidsDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

    }
}