package com.gauravsingh.gitfinder.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal class Contributor(
        @SerializedName("login")
        @Expose
        val login: String,
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("avatar_url")
        @Expose
        val avatarUrl: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeInt(id)
        parcel.writeString(avatarUrl)
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