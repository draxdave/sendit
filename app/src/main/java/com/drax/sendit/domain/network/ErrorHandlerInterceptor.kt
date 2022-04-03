package com.drax.sendit.domain.network

import android.content.res.Resources
import com.drax.sendit.R
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

class ErrorHandlerInterceptor(
    private val resources: Resources,
    ): Interceptor {
    private lateinit var request: Request

    override fun intercept(chain: Interceptor.Chain): Response {
        request = chain.request()

        val response = try {
            val initResponse = chain.proceed(request)

            when (initResponse.code) {
                400 -> {
                    Exception("Error 400:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        ConnectException,
                        resources.getString(R.string.error_internal)
                    )
                        .toResponse()

                }
                in 402..499 -> {
                    Exception("Error 402:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        ConnectException,
                        resources.getString(R.string.error_internal)
                    )
                        .toResponse()

                }
                in 300..399 -> {
                    Exception("Error 300:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        ConnectException,
                        resources.getString(R.string.error_internal)
                    )
                        .toResponse()


                }
                in 500..599 -> {
                    Exception("Error 500:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        ConnectException,
                        resources.getString(R.string.error_internal)
                    )
                        .toResponse()


                }
                else -> {
                    initResponse
                }
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            ErrorResponse(e.code(), resources.getString(R.string.network_unavailable))
                .toResponse()

        } catch (e: ConnectException) {
            e.printStackTrace()
            ErrorResponse(ConnectException, resources.getString(R.string.network_unavailable))
                .toResponse()

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            ErrorResponse(SocketTimeoutException, resources.getString(R.string.network_unavailable))
                .toResponse()

        }catch (e : UnknownHostException){
            e.printStackTrace()
            ErrorResponse(UnknownHostException, resources.getString(R.string.network_unavailable))
                .toResponse()

        } catch (e: Exception) {
            e.printStackTrace()
            ErrorResponse(Exception, resources.getString(R.string.unknown_error))
                .toResponse()
        }

        return response
    }

    private fun ErrorResponse.toResponse(): Response {
        val json = "{" +
                "\"error\":"+Json.encodeToString(this) +
                "}"

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("")
            .body( json.toResponseBody())
            .build()
    }

    companion object {
        const val ConnectException=600
        const val SocketTimeoutException=601
        const val UnknownHostException=602
        const val Exception=603
    }
}