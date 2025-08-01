package ir.mahan.ghabchin.data.repository

import ir.mahan.ghabchin.data.network.ApiServices
import javax.inject.Inject

class SplashRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getRandomPhoto() = api.fetchRandomPhoto()
}