package com.kunal.sunbase_task.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.kunal.sunbase_task.data.network.SafeApiRequest
import com.kunal.sunbase_task.data.network.apis.ImagesApi
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.data.room.AppDatabase
import com.kunal.sunbase_task.data.paging.PhotoPagingSource
import com.kunal.sunbase_task.data.paging.PhotosRemoteMediator

@OptIn(ExperimentalPagingApi::class)
class ImageRepository(
    private val imagesApi: ImagesApi,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {

    private val pagingSourceFactory = { appDatabase.getPhotosDao().getAllPhotos() }

    fun getPhotos(): LiveData<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            remoteMediator = PhotosRemoteMediator(
                imagesApi,
                appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    fun searchPhotos(query: String) = Pager(
        config = PagingConfig(
            pageSize = 8,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PhotoPagingSource(imagesApi, query = query) }
    ).liveData

}