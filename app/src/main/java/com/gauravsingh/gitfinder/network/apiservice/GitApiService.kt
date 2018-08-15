package com.gauravsingh.gitfinder.network.apiservice

import com.gauravsingh.gitfinder.model.GitSearch
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GitApiService {

    @GET("/search/repositories")
    fun searchRepo(
            @Query("q") q: String = "Sandeep",
            @Query("per_page") perPage: Int = 10): Flowable<GitSearch>
}