package ir.mahan.ghabchin.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ir.mahan.ghabchin.data.model.search.ResponseSearch.Result
import ir.mahan.ghabchin.data.network.ApiServices
import javax.inject.Inject

class SearchPagingSource @Inject constructor(private val api: ApiServices, private val query: String) :
    PagingSource<Int, Result>() {

    private val pageIndex = 1
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key ?: pageIndex
        return try {
            val  response = api.searchPhotos(query, page)
            val  data = response.body()!!.results!!

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