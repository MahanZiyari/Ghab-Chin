package ir.mahan.ghabchin.data.network

import ir.mahan.ghabchin.data.model.home.ResponseCategories
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.data.model.splash.ResponseRandom
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {

    @GET("photos/random")
    suspend fun fetchRandomPhoto(): Response<ResponseRandom>

    @GET("photos")
    suspend fun fetchLatestPhotos(): Response<ResponsePhotos>

    @GET("topics")
    suspend fun fetchCategoriesList(): Response<ResponseCategories>
}