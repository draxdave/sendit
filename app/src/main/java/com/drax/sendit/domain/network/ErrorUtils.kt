package com.drax.sendit.domain.network

import com.drax.sendit.domain.network.model.ErrorResponse
import org.json.JSONObject
import retrofit2.Response

class ErrorUtils {

    companion object {
        fun <T> Response<T>.parseError(): ErrorResponse? = if (!isSuccessful) {
            runCatching {
                JSONObject(errorBody()?.string())
                    .getJSONObject("error").run {
                    ErrorResponse(
                        type = getInt("type"),
                        description = getString("description")
                    )
                }
            }.getOrNull()
        } else {
            null
        }
    }
}
