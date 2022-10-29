package com.minabeshara.astroboom.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.minabeshara.astroboom.database.getDatabase
import com.minabeshara.astroboom.repository.AsteroidsRepository
import retrofit2.HttpException

class AsteroidsDataWorker(appContext: Context, parameters: WorkerParameters) :
    CoroutineWorker(appContext, parameters) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        try {
            repository.refreshAsteroids()
            repository.refreshPictureOfDay()
        }catch (e :HttpException){
            return Result.retry()
        }
        return Result.success()
    }

    companion object{
        const val WORK_NAME = "com.minabeshara.astroboom.work.AsteroidsDataWorker"
    }
}