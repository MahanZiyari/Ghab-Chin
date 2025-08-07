package ir.mahan.ghabchin.data.repository

import ir.mahan.ghabchin.data.network.ApiServices
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getLatestPhotos() = api.fetchLatestPhotos()
    suspend fun getCategories() = api.fetchCategoriesList()
}