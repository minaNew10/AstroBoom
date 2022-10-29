package com.minabeshara.astroboom.main

import android.app.Application
import androidx.lifecycle.*
import com.minabeshara.astroboom.database.getDatabase
import com.minabeshara.astroboom.model.Asteroid
import com.minabeshara.astroboom.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : ViewModel() {

    private val asteroidsRepository = AsteroidsRepository(getDatabase(app))

    val asteroids = asteroidsRepository.asteroids

    val pictureOfDay = asteroidsRepository.pictureOfDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails

    private val _progressDialogVisibility= MutableLiveData<Boolean>()
    val progressDialogVisibility
        get() = _progressDialogVisibility

    init {
        viewProgressDialog()
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshPictureOfDay()
        }
    }
    private fun viewProgressDialog(){
        _progressDialogVisibility.value = true
    }
    fun hideProgressDialog(){
        _progressDialogVisibility.value = false
    }




    fun onAsteroidClicked(asteroid: Asteroid){
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