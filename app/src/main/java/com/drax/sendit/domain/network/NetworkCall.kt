package com.drax.sendit.domain.network

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import okhttp3.Headers
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class NetworkCall<ResultType>(
    private val createCall: suspend () -> Response<ResultType>
) {


    suspend fun fetch(): Resource<ResultType> {

        val response = createCall()
        return  if (response.isSuccessful) {
            val body = response.body()

            if (body is ApiResponse<*>){
                if (body.statusCode == 200)
                    Resource.SUCCESS(body)

                else
                    Resource.ERROR(body.error.description, body.statusCode)

            }else
                Resource.SUCCESS(body)

        } else
            Resource.ERROR(errorCode = response.code())


    }

}