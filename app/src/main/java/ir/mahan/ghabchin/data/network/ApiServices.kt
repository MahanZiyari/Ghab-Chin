package ir.mahan.ghabchin.data.network

import ir.mahan.ghabchin.data.model.splash.ResponseRandom
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {

    @GET("photos/random")
    suspend fun fetchRandomPhoto(): Response<ResponseRandom>
}