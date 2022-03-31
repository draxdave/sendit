package com.drax.sendit.domain.network

import android.content.res.Resources
import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.ErrorResponse
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.DeviceInfoHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@DelicateCoroutinesApi
class AuthInterceptor(
    private val resources: Resources,
    private val authRepository: UserRepository,
    private val registryRepository: RegistryRepository,

    ): Interceptor {
    private lateinit var request: Request
    private val apiToken: String? by lazy {
        registryRepository.getApiToken()
    }

    private val headers by lazy {
        Headers.headersOf(
            "lang:en",
            "app-version:"+BuildConfig.VERSION_CODE,
            "region:hk",
            "device-platform:"+ DeviceInfoHelper.platform,
            "device-platform-version:"+DeviceInfoHelper.platformVersion,
            "device-model:"+DeviceInfoHelper.model,
            "api-key:$apiToken"
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        request = chain.request()
            .newBuilder()
            .headers(headers)
            .build()

        val response = try {
            val initResponse = chain.proceed(request)
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

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
//            .networkResponse(body.raw())
            .code(200)
            .message("")
            .body(Json.encodeToString(ApiResponse(this.type, Unit, this)).toResponseBody())
            .build()
    }

    private fun unAuthorizedAccessDetected(){
        GlobalScope.launch(Dispatchers.Default) {
            authRepository.clearDb()
        }
    }

    companion object {
        const val ConnectException=600
        const val SocketTimeoutException=601
        const val UnknownHostException=602
        const val Exception=603
    }
}