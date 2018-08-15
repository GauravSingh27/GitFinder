package com.gauravsingh.gitfinder.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class GitServer : Server {

    private val baseUrl = "https://api.github.com"

    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    private val gsonConverterFactory = GsonConverterFactory.create(gson)

    override fun <T> buildApi(service: Class<T>): T {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(service)
    }
}