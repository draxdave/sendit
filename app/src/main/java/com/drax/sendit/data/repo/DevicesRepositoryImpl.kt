package com.drax.sendit.data.repo

import com.drax.sendit.data.db.DevicesDao
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.*
import com.drax.sendit.domain.repo.DevicesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class DevicesRepositoryImpl(
    private val devicesDao: DevicesDao,
    private val apiService: ApiService
): DevicesRepository {

    override fun sendInvitation(pairRequest: PairRequest) = flow {
        emit(
            NetworkCall {
                apiService.pair(pairRequest)
            }.fetch()
        )
    }

    override fun invitationResponse(pairResponseRequest: PairResponseRequest) = flow {
        emit(
            NetworkCall {
                apiService.pairResponse(pairResponseRequest)
            }.fetch()
        )
    }

    override fun unpair(unpairRequest: UnpairRequest) = flow {
        emit(
            NetworkCall {
                apiService.unpair(unpairRequest)
            }.fetch()
        )
    }

    override fun getAllDevices() = devicesDao.getList()

    override suspend fun clearDb() {
        devicesDao.deleteAll()
    }
}