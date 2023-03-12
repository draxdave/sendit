package com.drax.sendit.domain.network

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.ErrorUtils.Companion.parseError
import com.drax.sendit.domain.network.model.ApiResponse
import retrofit2.Response


class NetworkCall<ResultType>(
    private val createCall: suspend () -> Response<ResultType>
) {

    suspend fun fetch(): Resource<ResultType> = try {
        val response = createCall()
        if (response.isSuccessful) {
            val body = response.body()

            if (body is ApiResponse<*>) {
                if (body.statusCode == 200)
                    Resource.SUCCESS(body)
                else
                    Resource.ERROR(body.statusCode)

            } else
                Resource.SUCCESS(body)

        } else {
            response.parseError()?.let {
                Resource.ERROR(it.type)

            } ?: Resource.ERROR(response.code())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.ERROR(-1)
    }
}