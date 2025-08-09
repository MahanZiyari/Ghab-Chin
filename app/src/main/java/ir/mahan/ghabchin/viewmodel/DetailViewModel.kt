package ir.mahan.ghabchin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.ghabchin.data.model.detail.ResponseDetail
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.data.repository.DetailRepository
import ir.mahan.ghabchin.data.repository.HomeRepository
import ir.mahan.ghabchin.util.network.ResponseHandler
import ir.mahan.ghabchin.util.network.Wrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {


    // Latest Photo
    private var _photoDetails = MutableLiveData<Wrapper<ResponseDetail>>()
    val photoDetails: LiveData<Wrapper<ResponseDetail>> = _photoDetails

    fun getPhotoDetailsBy(id: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _photoDetails.postValue(Wrapper.Loading())
            val response = repository.getPhotoDetail(id)
            _photoDetails.postValue(
                ResponseHandler(response).handleResponseCodes()
            )
        } catch (e: Exception) {
            _photoDetails.postValue(Wrapper.Error(e.message.toString()))
        }
    }



}