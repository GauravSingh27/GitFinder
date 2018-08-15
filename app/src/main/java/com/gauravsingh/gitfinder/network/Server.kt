package com.gauravsingh.gitfinder.network

internal interface Server {

    fun <T> buildApi(service: Class<T>): T
}