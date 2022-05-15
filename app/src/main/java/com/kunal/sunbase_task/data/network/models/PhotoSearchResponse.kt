package com.kunal.sunbase_task.data.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoSearchResponse(
    var results: List<Photo>
) : Parcelable
