package com.drax.sendit.view.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.drax.sendit.R
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.login.LoginUiState
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ConnectionsVM @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    deviceRepository: DeviceRepository,
    private val pushRepository: PushRepository
) : ResViewModel() {

    private val _uiState = MutableStateFlow<ConnectionUiState>(ConnectionUiState.Neutral)
    val uiState: StateFlow<ConnectionUiState> = _uiState


    val device: LiveData<Device?> = deviceRepository.getSelfDevice().asLiveData()

    init {
        job(Dispatchers.Default) {
            connectionRepository.getConnections().collect {connectionsList->
                _uiState.update {
                    when(connectionsList.isEmpty()){
                        true -> ConnectionUiState.NoConnection
                        false -> ConnectionUiState.ConnectionsLoaded(connectionsList)
                    }
                }
            }
        }

    }

    fun removeDevice(unpairRequest: UnpairRequest){
        job {
            connectionRepository.unpair(unpairRequest)
        }
    }

    fun getConnectionsFromServer(){
        _uiState.update { ConnectionUiState.RefreshingConnectionList }
        job {
            connectionRepository.getConnectionsFromServer().collect {getConnections->
                _uiState.update {
                    when(getConnections){
                        is Resource.ERROR -> ConnectionUiState.RefreshConnectionListFailed(getConnections)
                        is Resource.SUCCESS -> {
                            val newConnections = getConnections.data.data?.connections
                            when {
                                newConnections == null -> ConnectionUiState.RefreshConnectionListFailed(Resource.ERROR(errorCode = R.string.unknown_error))
                                newConnections.isEmpty() -> ConnectionUiState.RefreshConnectionListSucceedButEmpty
                                else -> {
                                    connectionRepository.clearDb()
                                    connectionRepository.addConnection(*newConnections.toTypedArray())
                                    ConnectionUiState.RefreshConnectionListSucceed(newConnections)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}