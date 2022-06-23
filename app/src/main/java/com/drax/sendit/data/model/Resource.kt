package com.drax.sendit.data.model

sealed class Resource<in T> {
    data class SUCCESS<T>(val data: T) : Resource<T>()
    data class ERROR(val errorCode : Int) : Resource<Any?>()
}
