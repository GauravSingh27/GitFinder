package com.gauravsingh.gitfinder.network.apiservice

import com.gauravsingh.gitfinder.model.Contributor
import com.gauravsingh.gitfinder.model.GitRepo
import com.gauravsingh.gitfinder.model.GitSearch
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitApiService {

    @GET("/search/repositories")
    fun searchRepo(
            @Query("q") query: String = "Sandeep",
            @Query("sort") sort: String? = null,
            @Query("order") order: String = "desc",
            @Query("per_page") perPage: Int = 10): Single<GitSearch>


    @GET("/repos/{owner}/{name}/contributors")
    fun fetchRepoContributors(@Path("owner") owner: String, @Path("name") name: String): Single<List<Contributor>>

    @GET("/users/{userLoginName}")
    fun fetchUser(@Path("userLoginName") userLoginName: String): Single<Contributor>

    @GET("/users/{userLoginName}/repos")
    fun fetchUserRepos(@Path("userLoginName") userLoginName: String): Flowable<List<GitRepo>>
}