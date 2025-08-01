package ir.mahan.ghabchin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.ghabchin.data.model.splash.ResponseRandom
import ir.mahan.ghabchin.data.repository.SplashRepository
import ir.mahan.ghabchin.util.network.ResponseHandler
import ir.mahan.ghabchin.util.network.Wrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashRepository) : ViewModel() {

    private var _randomPhoto = MutableLiveData<Wrapper<ResponseRandom>>()
    val randomPhoto: LiveData<Wrapper<ResponseRandom>> = _randomPhoto

    fun getRandomPhoto() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _randomPhoto.postValue(Wrapper.Loading())
            val response = repository.getRandomPhoto()
            _randomPhoto.postValue(
                ResponseHandler(response).handleResponseCodes()
            )
        } catch (e: Exception) {
            _randomPhoto.postValue(Wrapper.Error(e.message.toString()))
        }
    }
}