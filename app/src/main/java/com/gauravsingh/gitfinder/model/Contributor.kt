package com.gauravsingh.gitfinder.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class Contributor(
        @SerializedName("login")
        @Expose
        val login: String,
        @SerializedName("name")
        @Expose
        val name: String?,
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("avatar_url")
        @Expose
        val avatarUrl: String,
        @SerializedName("html_url")
        @Expose
        val htmlUrl: String,
        @SerializedName("url")
        @Expose
        val url: String,
        @SerializedName("repos_url")
        @Expose
        val reposUrl: String,
        @SerializedName("contributions")
        @Expose
        val contributions: Int,
        @SerializedName("followers")
        @Expose
        val followers: Int,
        @SerializedName("following")
        @Expose
        val following: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(name)
        parcel.writeInt(id)
        parcel.writeString(avatarUrl)
        parcel.writeString(htmlUrl)
        parcel.writeString(url)
        parcel.writeString(reposUrl)
        parcel.writeInt(contributions)
        parcel.writeInt(followers)
        parcel.writeInt(following)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contributor> {
        override fun createFromParcel(parcel: Parcel): Contributor {
            return Contributor(parcel)
        }

        override fun newArray(size: Int): Array<Contributor?> {
            return arrayOfNulls(size)
        }
    }
}