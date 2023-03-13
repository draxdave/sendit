package com.drax.sendit.data.repo

import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.ShareRequest
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.RegistryRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow

@Singleton
class PushRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val registryRepository: RegistryRepository,
    private val deviceRepository: DeviceRepository
) : PushRepository {

    override fun shareContent(shareRequest: ShareRequest) = flow {
        emit(
            NetworkCall {
                apiService.share(shareRequest)
            }.fetch()
        )
    }

}