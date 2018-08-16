package com.gauravsingh.gitfinder.datasource

import com.gauravsingh.gitfinder.model.Contributor
import com.gauravsingh.gitfinder.model.GitRepo
import com.gauravsingh.gitfinder.network.GitServer
import com.gauravsingh.gitfinder.network.apiservice.GitApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class GitDataSource : DataSource {

    private val defaultSearchString = "Sandeep"
    private val defaultOrder = "desc"
    private val gitApiService = GitServer().buildApi(GitApiService::class.java)

    fun searchRepository(
            searchString: String = defaultSearchString,
            sort: String? = null,
            order: String = defaultOrder): Single<List<GitRepo>> {

        return gitApiService.searchRepository(searchString, sort, order)
                .subscribeOn(Schedulers.io()).map { gitSearch -> gitSearch.repos }
    }

    fun fetchRepositoryContributors(
            repositoryOwnerName: String,
            repositoryName: String): Single<List<Contributor>> {

        return gitApiService
                .fetchRepositoryContributors(repositoryOwnerName, repositoryName)
                .subscribeOn(Schedulers.io())
    }

    fun fetchUser(userLoginName: String): Single<Contributor> {

        return gitApiService.fetchUser(userLoginName).subscribeOn(Schedulers.io())
    }

    fun fetchUserRepository(userLoginName: String): Single<List<GitRepo>> {
        return gitApiService.fetchUserRepository(userLoginName).subscribeOn(Schedulers.io())
    }
}