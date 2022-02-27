package com.drax.sendit.data.repo

import android.os.Build
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.Content
import com.drax.sendit.domain.network.model.FirebaseSendRequest
import com.drax.sendit.domain.network.model.FirebaseSendResponse
import com.drax.sendit.domain.network.model.Invitation
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import retrofit2.Response

class PushRepositoryImp(private val push: ApiService, private val registryRepository: RegistryRepository, private val devicesRepository: DevicesRepository)
    : PushRepository {

    override suspend fun sendInvitation(destinationId: String) : Resource<FirebaseSendResponse>? {
        return registryRepository.getFirebaseId()?.let {token->

            object : NetworkCall<FirebaseSendResponse>() {
                override suspend fun createCall(): Response<FirebaseSendResponse> {

                    return push.sendInvitation(
                        FirebaseSendRequest(
                            registration_ids = listOf(destinationId),
                            collapse_key = "type_a",
                            data = Invitation(
                                message = "${Build.MANUFACTURER} ${Build.MODEL}, ${Build.VERSION.RELEASE}",
                                senderToken = token,
                                sent = System.currentTimeMillis()
                            )
                        )
                    )
                }

            }.fetch()

        }
    }

    override suspend fun sendContent(content: String,destinationIds: List<String>) : Resource<FirebaseSendResponse>?  {
        return registryRepository.getFirebaseId()?.let {token->

            object : NetworkCall<FirebaseSendResponse>() {
                override suspend fun createCall(): Response<FirebaseSendResponse> {

                    return push.sendContent(
                        FirebaseSendRequest(
                            registration_ids = destinationIds,
                            collapse_key = "type_a",
                            data = Content(
                                message = content,
                                senderToken = token,
                                sent = System.currentTimeMillis()
                            )
                        )
                    )
                }

            }.fetch()
        }
    }

    override suspend fun sendContentToAll(content: String) : Resource<FirebaseSendResponse>?  {
        return sendContent(content, devicesRepository.getAllDevicesSync().map { it.instanceId })
    }
}