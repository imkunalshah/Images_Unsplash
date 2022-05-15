package com.kunal.sunbase_task.data.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kunal.sunbase_task.data.network.models.Photo

@Dao
interface PhotosDao {

    @Query("SELECT * FROM " + "photo_table")
    fun getAllPhotos(): PagingSource<Int, Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllPhotos(photos: List<Photo>): LongArray

    @Query("DELETE FROM photo_table")
    suspend fun deleteAllPhotos()
}