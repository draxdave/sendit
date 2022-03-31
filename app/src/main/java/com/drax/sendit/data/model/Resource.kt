package com.drax.sendit.data.model

sealed class Resource<in T> {
    data class SUCCESS<T>(val data: T) : Resource<T>()
    data class ERROR(val message: String? = "", val errorCode : Int = 0) : Resource<Any?>()
}
