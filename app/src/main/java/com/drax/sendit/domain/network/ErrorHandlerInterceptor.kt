package com.drax.sendit.domain.network

import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.ErrorResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandlerInterceptor @Inject constructor(
    private val json: Json,
    private val analytics: Analytics
) : Interceptor {
    private lateinit var request: Request

    override fun intercept(chain: Interceptor.Chain): Response {
        request = chain.request()
        analytics.set(Event.Network.ApiRequest(request.url.fragment ?: ""))

        val response = try {
            return chain.proceed(request)

        } catch (e: HttpException) {
            e.printStackTrace()
            ErrorResponse(e.code()).toResponse()

        } catch (e: ConnectException) {
            e.printStackTrace()
            ErrorResponse(ConnectException).toResponse()

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            ErrorResponse(SocketTimeoutException).toResponse()

        } catch (e: UnknownHostException) {
            e.printStackTrace()
            ErrorResponse(UnknownHostException).toResponse()

        } catch (e: Exception) {
            e.printStackTrace()
            ErrorResponse(Exception).toResponse()
        }

        return response
    }

    private fun ErrorResponse.toResponse(): Response {
        analytics.set(Event.Network.ApiError(type.toString(), request.url.fragment ?: ""))

        val responseStr = "{\"statusCode\":$type," +
                "\"error\":" + json.encodeToString(this) +
                "}"

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("")
            .body(responseStr.toResponseBody())
            .build()
    }

    companion object {
        const val ConnectException = 600
        const val SocketTimeoutException = 601
        const val UnknownHostException = 602
        const val Exception = 603
        const val BadRequest = 400
        val UnControlledBadRequest = 402..499
        val UnControlledRedirection = 300..399
        val UnControlledServerError = 500..599
    }
}