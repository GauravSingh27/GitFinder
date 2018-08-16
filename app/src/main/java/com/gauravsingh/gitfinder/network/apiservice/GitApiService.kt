package com.gauravsingh.gitfinder.network.apiservice

import com.gauravsingh.gitfinder.model.Contributor
import com.gauravsingh.gitfinder.model.GitRepo
import com.gauravsingh.gitfinder.model.GitSearch
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitApiService {

    @GET("/search/repositories")
    fun searchRepository(
            @Query("q") searchString: String,
            @Query("sort") sort: String?,
            @Query("order") order: String,
            @Query("per_page") perPage: Int = 10): Single<GitSearch>


    @GET("/repos/{repositoryOwnerName}/{repositoryName}/contributors")
    fun fetchRepositoryContributors(
            @Path("repositoryOwnerName") repositoryOwnerName: String,
            @Path("repositoryName") repositoryName: String): Single<List<Contributor>>

    @GET("/users/{userLoginName}")
    fun fetchUser(@Path("userLoginName") userLoginName: String): Single<Contributor>

    @GET("/users/{userLoginName}/repos")
    fun fetchUserRepository(@Path("userLoginName") userLoginName: String): Single<List<GitRepo>>
}