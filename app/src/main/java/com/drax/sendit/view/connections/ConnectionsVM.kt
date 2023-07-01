package com.drax.sendit.view.connections

import androidx.compose.runtime.mutableStateOf
import app.siamak.sendit.R
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.network.model.type.ConnectionRole
import com.drax.sendit.domain.network.model.type.ConnectionStatus
import com.drax.sendit.domain.network.model.type.ConnectionType
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.domain.network.model.type.DeviceStatus
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@HiltViewModel
class ConnectionsVM @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    deviceRepository: DeviceRepository,
    userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ResViewModel() {

    var deviceInfo = mutableStateOf<DeviceUiModel?>(null)

    var uiState = mutableStateOf<ConnectionUiState>(
        ConnectionUiState.ConnectionsLoaded(TEMP_DEVICES)
    )

    private val user = userRepository.getUser()
    var userInfo = mutableStateOf<User?>(null)
    private val device = deviceRepository.getSelfDevice()

    init {
        job {
            connectionRepository.getConnections(onlyActive = false).collect { connectionsList ->
                withContext(Dispatchers.Main) {
                    uiState.value = when (connectionsList.isEmpty()) {
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

        job(dispatcher = Dispatchers.Main) {
            device.filterNotNull()
                .map {
                    DeviceTransformer.toUiModel(it)
                }
                .collect {
                    deviceInfo.value = it
                }
        }
        job(dispatcher = Dispatchers.Main) {
            user.collect {
                userInfo.value = it
            }
        }
        getConnectionsFromServer()
    }

    fun getConnectionsFromServer() {
        uiState.value = ConnectionUiState.Refreshing
        job {
            connectionRepository.getConnectionsFromServer().collect { getConnections ->

                val newState = when (getConnections) {
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

                withContext(Dispatchers.Main) {
                    uiState.value = newState
                }
            }
        }
    }

    fun signOut() {
        uiState.value = ConnectionUiState.Refreshing
        job {
            authRepository.signOutDevice().collect()
        }
    }

    private suspend fun emptyConnections() = connectionRepository.emptyConnections()

    companion object {
        const val DEFAULT_DEVICE_PLACEHOLDER = R.drawable.default_device_placeholder
        val TEMP_DEVICES = listOf(
            DeviceWrapper(
                Connection(
                    id = 1,
                    name = "iPhone 12 Pro",
                    connectDate = 1348333333,
                    lastUsed = 1348333333,
                    status = ConnectionStatus.ConnectionStatus_ACTIVE,
                    meta = "",
                    iconUrl = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50",
                    platform = DevicePlatform.DevicePlatform_ANDROID,
                    platformVersion = "11",
                    deviceStatus = DeviceStatus.DeviceStatus_ACTIVE,
                    model = "As",
                    role = ConnectionRole.ROLE_CONNECTEE,
                    type = ConnectionType.ConnectionType_BLOCKED,
                )
            ),
            DeviceWrapper(
                Connection(
                    id = 1,
                    name = "iPhone 12 Pro",
                    connectDate = 1348333333,
                    lastUsed = 1348333333,
                    status = ConnectionStatus.ConnectionStatus_ACTIVE,
                    meta = "",
                    iconUrl = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50",
                    platform = DevicePlatform.DevicePlatform_ANDROID,
                    platformVersion = "11",
                    deviceStatus = DeviceStatus.DeviceStatus_ACTIVE,
                    model = "As",
                    role = ConnectionRole.ROLE_CONNECTEE,
                    type = ConnectionType.ConnectionType_BLOCKED,
                )
            )
        )
    }
}
