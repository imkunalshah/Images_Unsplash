package com.kunal.sunbase_task.data.room.type_converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunal.sunbase_task.data.network.models.PhotoUrls
import java.lang.reflect.Type


class PhotoUrlsConverter {

    private val gSon = Gson()

    private val type: Type = object : TypeToken<PhotoUrls?>() {}.type

    @TypeConverter
    fun from(photoUrls: PhotoUrls?): String? {
        if (photoUrls == null) {
            return null
        }

        return gSon.toJson(photoUrls, type)
    }

    @TypeConverter
    fun to(photoUrlString: String?): PhotoUrls? {
        if (photoUrlString == null) {
            return null
        }
        return gSon.fromJson<PhotoUrls>(photoUrlString, type)
    }

}