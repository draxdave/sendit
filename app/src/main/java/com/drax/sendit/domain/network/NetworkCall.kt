package com.drax.sendit.domain.network

import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.Status
import okhttp3.Headers
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


//todo : inject Gson by parameter
abstract class NetworkCall<ResultType> {
    suspend fun fetch(): Resource<ResultType> {
        return try {
            val response = createCall()
            if (response.isSuccessful) {
                onSuccess(response.body(),response.headers())
                Resource(Status.SUCCESS, response.body(),"")

            } else{
                val errorMsg = response.errorBody()?.let {
                    JSONObject(it.charStream().readText()).getString("message")
                }

                Resource.error(errorMsg?:"" ,null, response.code())
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.error("", null, e.code())

        } catch (e: ConnectException) {
            e.printStackTrace()
            Resource.error("",null, ConnectException)

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Resource.error("",null, SocketTimeoutException)

        }catch (e : UnknownHostException){
            e.printStackTrace()
            Resource.error("",null, UnknownHostException)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.error("",null, Exception)
        }
    }

    abstract suspend fun createCall(): Response<ResultType>
    open suspend fun onSuccess(result: ResultType?, headers: Headers){}
    companion object{
        final const val ConnectException=600
        final const val SocketTimeoutException=601
        final const val UnknownHostException=602
        final const val Exception=603
    }
}