package ir.mahan.ghabchin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ir.mahan.ghabchin.data.network.ApiServices
import ir.mahan.ghabchin.data.source.CategoryPagingSource
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val api: ApiServices) {

    fun getCategoryPhotos(id: String) = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = {
            CategoryPagingSource(api, id)
        }
    ).liveData
}