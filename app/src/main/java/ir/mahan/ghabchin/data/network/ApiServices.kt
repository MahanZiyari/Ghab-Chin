package ir.mahan.ghabchin.data.network

import ir.mahan.ghabchin.data.model.home.ResponseCategories
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.data.model.search.ResponseSearch
import ir.mahan.ghabchin.data.model.splash.ResponseRandom
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("photos/random")
    suspend fun fetchRandomPhoto(): Response<ResponseRandom>

    @GET("photos")
    suspend fun fetchLatestPhotos(): Response<ResponsePhotos>

    @GET("topics")
    suspend fun fetchCategoriesList(): Response<ResponseCategories>

    @GET("topics/{id}/photos")
    suspend fun fetchCategoryPhotos(@Path("id") id: String, @Query("page") page: Int): Response<ResponsePhotos>

    @GET("search/photos")
    suspend fun searchPhotos(@Query("query") query: String, @Query("page") page: Int): Response<ResponseSearch>
}