package com.minabeshara.astroboom.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minabeshara.astroboom.database.getDatabase
import com.minabeshara.astroboom.model.Asteroid
import com.minabeshara.astroboom.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : ViewModel() {

    private val asteroidsRepository = AsteroidsRepository(getDatabase(app))

    val newAsteroids = asteroidsRepository.asteroids
    val oldAsteroids = asteroidsRepository.oldAsteroids
    val pictureOfDay = asteroidsRepository.pictureOfDay

    private val _asteroidsInDay = MutableLiveData<List<Asteroid>>()
    val asteroidsInDay
        get() = _asteroidsInDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails

    private val _progressDialogVisibility = MutableLiveData<Boolean>()
    val progressDialogVisibility
        get() = _progressDialogVisibility

    init {
        viewProgressDialog()
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshPictureOfDay()
        }
    }

    fun getAsteroidsInDay(day :String){
        viewModelScope.launch {
            val asteroids = asteroidsRepository.getAsteroidsInDay(day)
            _asteroidsInDay.value = asteroids
        }
    }
    private fun viewProgressDialog() {
        _progressDialogVisibility.value = true
    }

    fun hideProgressDialog() {
        _progressDialogVisibility.value = false
    }


    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidDetailsNavigated() {
        _navigateToAsteroidDetails.value = null
    }

    class ViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}