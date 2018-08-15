package com.gauravsingh.gitfinder.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class GitRepo(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("node_id")
        @Expose
        val nodeId: String,
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("full_name")
        @Expose
        val fullName: String,
        @SerializedName("owner")
        @Expose
        val owner: Contributor,
        @SerializedName("html_url")
        @Expose
        val htmlUrl: String,
        @SerializedName("description")
        @Expose
        val description: String,
        @SerializedName("url")
        @Expose
        val url: String,
        @SerializedName("size")
        @Expose
        val size: Int,
        @SerializedName("stargazers_count")
        @Expose
        val stargazersCount: Int,
        @SerializedName("watchers_count")
        @Expose
        val watchersCount: Int,
        @SerializedName("forks_count")
        @Expose
        val forksCount: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Contributor::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nodeId)
        parcel.writeString(name)
        parcel.writeString(fullName)
        parcel.writeParcelable(owner, flags)
        parcel.writeString(htmlUrl)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeInt(size)
        parcel.writeInt(stargazersCount)
        parcel.writeInt(watchersCount)
        parcel.writeInt(forksCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GitRepo> {
        override fun createFromParcel(parcel: Parcel): GitRepo {
            return GitRepo(parcel)
        }

        override fun newArray(size: Int): Array<GitRepo?> {
            return arrayOfNulls(size)
        }
    }
}