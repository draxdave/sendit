package com.drax.sendit.domain.network

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import retrofit2.Response


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
                    Resource.ERROR(body.statusCode)

            }else
                Resource.SUCCESS(body)

        } else
            Resource.ERROR(response.code())


    }

}