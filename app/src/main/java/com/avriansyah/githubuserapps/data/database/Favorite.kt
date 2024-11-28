package com.avriansyah.githubuserapps.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite_users")
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    var login: String = "",
    var avatarUrl: String? = null,
): Parcelable
