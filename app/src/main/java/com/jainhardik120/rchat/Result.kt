package com.jainhardik120.rchat


sealed class Result<T, R>(val data:T?=null, val errorMessage : String?=null, val errorBody : R?=null){
    class Success<T, R>(data: T) : Result<T, R>(data = data)
    class ClientException<T, R>(errorBody: R? = null):Result<T,R>(errorBody = errorBody)
    class Exception<T,R>(message : String?) : Result<T,R>(errorMessage = message)
}
