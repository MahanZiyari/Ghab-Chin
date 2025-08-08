package ir.mahan.ghabchin.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.data.network.ApiServices
import javax.inject.Inject

class CategoryPagingSource @Inject constructor(private val api: ApiServices, private val id: String) :
    PagingSource<Int, ResponsePhotos.ResponsePhotosItem>() {

    private val pageIndex = 1
    override fun getRefreshKey(state: PagingState<Int, ResponsePhotos.ResponsePhotosItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponsePhotos.ResponsePhotosItem> {
        val page = params.key ?: pageIndex
        return try {
            val  response = api.fetchCategoryPhotos(id, page)
            val  data = response.body()!!

            LoadResult.Page(
                data = data,
                prevKey = if (page == pageIndex) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}