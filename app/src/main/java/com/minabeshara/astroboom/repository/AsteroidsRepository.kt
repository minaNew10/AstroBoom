package com.minabeshara.astroboom.repository

import androidx.lifecycle.LiveData
import com.minabeshara.astroboom.BuildConfig
import com.minabeshara.astroboom.api.NasaApi
import com.minabeshara.astroboom.database.AsteroidsDatabase
import com.minabeshara.astroboom.model.Asteroid
import com.minabeshara.astroboom.model.PictureOfDay
import com.minabeshara.astroboom.utils.Constants
import com.minabeshara.astroboom.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids()
    val pictureOfDay : LiveData<PictureOfDay> = database.asteroidDao.getPictureOfDay()
    suspend fun refreshAsteroids() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val endDayTime = calendar.time
        val endDay = dateFormat.format(endDayTime)
        withContext(Dispatchers.IO) {

            val jsonResult = NasaApi.retrofitService.getAsteroids(
                BuildConfig.API_KEY, today, endDay
            )
            val list = parseAsteroidsJsonResult(JSONObject(jsonResult))
            database.asteroidDao.insertAll(list)
        }
    }
    suspend fun refreshPictureOfDay(){
        withContext(Dispatchers.IO){
            val pic = NasaApi.retrofitService.getPictureOfDay(
                BuildConfig.API_KEY
            )
            database.asteroidDao.insertPicture(pic)
        }
    }
}