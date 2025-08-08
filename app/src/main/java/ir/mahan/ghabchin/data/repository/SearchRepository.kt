package ir.mahan.ghabchin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ir.mahan.ghabchin.data.network.ApiServices
import ir.mahan.ghabchin.data.source.CategoryPagingSource
import ir.mahan.ghabchin.data.source.SearchPagingSource
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: ApiServices) {

    fun searchPhotos(query: String) = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = {
            SearchPagingSource(api, query)
        }
    ).liveData
}