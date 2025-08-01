package ir.mahan.ghabchin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.data.model.splash.ResponseRandom
import ir.mahan.ghabchin.data.repository.HomeRepository
import ir.mahan.ghabchin.data.repository.SplashRepository
import ir.mahan.ghabchin.util.network.ResponseHandler
import ir.mahan.ghabchin.util.network.Wrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private var _latestPhotos = MutableLiveData<Wrapper<ResponsePhotos>>()
    val latestPhotos: LiveData<Wrapper<ResponsePhotos>> = _latestPhotos

    fun getLatestPhotos() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _latestPhotos.postValue(Wrapper.Loading())
            val response = repository.getLatestPhotos()
            _latestPhotos.postValue(
                ResponseHandler(response).handleResponseCodes()
            )
        } catch (e: Exception) {
            _latestPhotos.postValue(Wrapper.Error(e.message.toString()))
        }
    }
}