package com.gauravsingh.gitfinder.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class GitSearch(

        @SerializedName("total_count")
        @Expose
        val totalCount: Int,
        @SerializedName("incomplete_results")
        @Expose
        val incompleteResults: Boolean,
        @SerializedName("items")
        @Expose
        val repos: List<GitRepo>)