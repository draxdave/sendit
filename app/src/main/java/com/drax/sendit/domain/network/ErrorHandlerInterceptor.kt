package com.drax.sendit.domain.network

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
    private val json: Json
    ): Interceptor {
    private lateinit var request: Request

    override fun intercept(chain: Interceptor.Chain): Response {
        request = chain.request()

        val response = try {
            val initResponse = chain.proceed(request)

            when (initResponse.code) {
                BadRequest -> {
                    Exception("Error 400:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        BadRequest,
                    )
                        .toResponse()

                }
                in UnControlledBadRequest -> {
                    Exception("Error 402:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        BadRequest,
                    )
                        .toResponse()

                }
                in UnControlledRedirection -> {
                    Exception("Error 300:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse( // Missing Required field(s
                        UnControlledRedirection.first
                    )
                        .toResponse()


                }
                in UnControlledServerError -> {
                    Exception("Error 500:" + initResponse.body?.string()).printStackTrace()
                    ErrorResponse(UnControlledServerError.first).toResponse()


                }
                else -> {
                    initResponse
                }
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            ErrorResponse(e.code()).toResponse()

        } catch (e: ConnectException) {
            e.printStackTrace()
            ErrorResponse(ConnectException).toResponse()

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            ErrorResponse(SocketTimeoutException).toResponse()

        }catch (e : UnknownHostException){
            e.printStackTrace()
            ErrorResponse(UnknownHostException).toResponse()

        } catch (e: Exception) {
            e.printStackTrace()
            ErrorResponse(Exception).toResponse()
        }

        return response
    }

    private fun ErrorResponse.toResponse(): Response {
        val responseStr = "{\"statusCode\":$type," +
                "\"error\":"+json.encodeToString(this) +
                "}"

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("")
            .body( responseStr.toResponseBody())
            .build()
    }

    companion object {
        const val ConnectException=600
        const val SocketTimeoutException=601
        const val UnknownHostException=602
        const val Exception = 603
        const val BadRequest = 400
        val UnControlledBadRequest = 402..499
        val UnControlledRedirection = 300..399
        val UnControlledServerError = 500..599
    }
}