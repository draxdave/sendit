package com.drax.sendit.data.repo

import android.os.Build
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.*
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.RegistryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val registryRepository: RegistryRepository,
    private val devicesRepository: DevicesRepository)
    : PushRepository {

    override fun shareContent(shareRequest: ShareRequest) = flow {
        emit(
            NetworkCall {
                apiService.share(shareRequest)
            }.fetch()
        )
    }

}