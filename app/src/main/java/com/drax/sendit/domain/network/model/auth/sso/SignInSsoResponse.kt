package com.drax.sendit.domain.network.model.auth.sso

import kotlinx.serialization.Serializable

@Serializable
data class SignInSsoResponse(
    val token: String
) {
    companion object {
        const val DEVICE_IS_NOT_ACTIVE = 1805
        const val INCORRECT_CREDENTIALS = 1818
        const val USER_IS_NOT_ACTIVE = 1804
        const val USER_ALREADY_ACTIVE = 1904
    }
}

