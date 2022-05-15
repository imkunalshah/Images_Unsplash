package com.kunal.sunbase_task.data.network.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photo_table")
data class Photo(
    val urls: PhotoUrls,
    @PrimaryKey(autoGenerate = false)
    val id: String
) : Parcelable
