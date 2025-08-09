package ir.mahan.ghabchin.data.repository

import ir.mahan.ghabchin.data.network.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getPhotoDetail(id: String) = api.fetchPhotoDetail(id)
}