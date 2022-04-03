package com.drax.sendit.view.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.job
import javax.inject.Inject

class ConnectionsVM @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    private val pushRepository: PushRepository
) : ViewModel() {

    val devices: LiveData<List<Connection>> = connectionRepository.getConnections()
        .asLiveData()

    fun removeDevice(unpairRequest: UnpairRequest){
        job {
            connectionRepository.unpair(unpairRequest)
        }
    }
}