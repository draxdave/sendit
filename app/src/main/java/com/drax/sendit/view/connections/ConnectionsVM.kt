package com.drax.sendit.view.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.DeviceWrapper
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ConnectionsVM @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    deviceRepository: DeviceRepository,
    userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ResViewModel() {

    private val _uiState = MutableStateFlow<ConnectionUiState>(ConnectionUiState.Neutral)
    val uiState: StateFlow<ConnectionUiState> = _uiState
    val user: LiveData<User?> = userRepository.getUser().asLiveData()
    val device = deviceRepository.getSelfDevice()

    init {
        job(Dispatchers.Default) {
            connectionRepository.getConnections(onlyActive = false).collect { connectionsList ->
                _uiState.update {
                    when (connectionsList.isEmpty()) {
                        true -> ConnectionUiState.NoConnection
                        false -> ConnectionUiState.ConnectionsLoaded(
                            connectionsList.map { connection ->
                                DeviceWrapper(
                                    connection
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    fun getConnectionsFromServer() {
        _uiState.update { ConnectionUiState.RefreshingConnectionList }
        job {
            connectionRepository.getConnectionsFromServer().collect { getConnections ->
                _uiState.update {
                    when (getConnections) {
                        is Resource.ERROR -> ConnectionUiState.RefreshConnectionListFailed(
                            getConnections
                        )
                        is Resource.SUCCESS -> {
                            val newConnections = getConnections.data.data?.connections
                            when {
                                newConnections == null -> ConnectionUiState.RefreshConnectionListFailed(
                                    Resource.ERROR(errorCode = R.string.unknown_error)
                                )
                                newConnections.isNotEmpty() -> {
                                    emptyConnections()
                                    connectionRepository.addConnection(*newConnections.toTypedArray())
                                    ConnectionUiState.ConnectionsLoaded(
                                        newConnections.map { connection ->
                                            DeviceWrapper(
                                                connection
                                            )
                                        }
                                    )
                                }
                                else -> {
                                    emptyConnections()
                                    ConnectionUiState.NoConnection
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun signOut() {
        job {
            authRepository.signOutDevice().collect()
        }
    }

    private suspend fun emptyConnections() = connectionRepository.emptyConnections()

    companion object {
        const val DEFAULT_DEVICE_PLACEHOLDER = R.drawable.default_device_placeholder
    }
}
