package com.minabeshara.astroboom.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minabeshara.astroboom.BuildConfig
import com.minabeshara.astroboom.api.NasaApi
import com.minabeshara.astroboom.model.Asteroid
import com.minabeshara.astroboom.model.PictureOfDay
import com.minabeshara.astroboom.utils.Constants
import com.minabeshara.astroboom.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    private val _imageOfDay = MutableLiveData<PictureOfDay>()

    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    init {
        getAsteroids()
        getImageOfDay()
    }

    private fun getAsteroids(){
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val endDayTime = calendar.time
        val endDay = dateFormat.format(endDayTime)
        viewModelScope.launch {
            val jsonResult = NasaApi.retrofitService.getAsteroids(
                BuildConfig.API_KEY,
                today,
                endDay
            )
            Log.i("TAG", "getAsteroids: ")
            _response.value = jsonResult
        }
    }

    private fun getImageOfDay(){
        viewModelScope.launch {
            _imageOfDay.value  = NasaApi.retrofitService.getImageOfDay(
                BuildConfig.API_KEY
            )
        }
    }

}