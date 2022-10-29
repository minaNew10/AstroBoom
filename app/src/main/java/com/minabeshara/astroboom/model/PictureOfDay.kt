package com.minabeshara.astroboom.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        @PrimaryKey val url: String) : Parcelable
