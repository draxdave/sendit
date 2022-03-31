package com.drax.sendit.domain.network

import android.content.res.Resources
import com.drax.sendit.R
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.ErrorResponse
import com.drax.sendit.domain.repo.AuthRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthInterceptor(
    private val resources: Resources,
    private val authRepository: AuthRepository,

): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val response = try {
            val initResponse = chain.call().execute()
            if (initResponse.isSuccessful) {

                val initBody = initResponse.body
                if (initBody != null)
                    Json.decodeFromString<ApiResponse<*>>(initBody.string()).let { apiResponse->
                        when (apiResponse.statusCode) {
                            400 -> ErrorResponse(
                                ConnectException,
                                resources.getString(R.string.error_internal)
                            )
                                .toResponse()

                            401 -> {
                                unAuthorizedAccessDetected()
                                ErrorResponse(
                                    ConnectException,
                                    resources.getString(R.string.error_unauthorized)
                                )
                                    .toResponse()
                            }

                            else -> initResponse.newBuilder().body(initBody).build()
                        }
                    }
                else
                    initResponse.newBuilder().body(initBody).build()

            } else
                initResponse

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
        val body = retrofit2.Response.success(ApiResponse(this.type, Unit, this))

        return Response.Builder()
            .networkResponse(body.raw())
            .build()
    }

    private fun unAuthorizedAccessDetected(){
        authRepository.signOutDevice()
    }

    companion object {
        const val ConnectException=600
        const val SocketTimeoutException=601
        const val UnknownHostException=602
        const val Exception=603
    }
}