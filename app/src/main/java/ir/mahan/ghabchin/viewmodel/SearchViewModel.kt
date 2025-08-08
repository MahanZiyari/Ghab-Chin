package ir.mahan.ghabchin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._ir_mahan_ghabchin_util_di_NetworkModule
import ir.mahan.ghabchin.data.model.home.ResponseCategories
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.data.model.splash.ResponseRandom
import ir.mahan.ghabchin.data.repository.CategoryRepository
import ir.mahan.ghabchin.data.repository.HomeRepository
import ir.mahan.ghabchin.data.repository.SearchRepository
import ir.mahan.ghabchin.data.repository.SplashRepository
import ir.mahan.ghabchin.util.network.ResponseHandler
import ir.mahan.ghabchin.util.network.Wrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository, private val state: SavedStateHandle) : ViewModel() {

    private val currentData = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val searchPhotos = currentData.switchMap { query ->
        repository.searchPhotos(query).cachedIn(viewModelScope)
    }

    fun updateSearchQuery(id: String) {
        currentData.value = id
    }

    companion object {
        const val CURRENT_QUERY = "current_query"
        const val DEFAULT_QUERY = ""
    }
}