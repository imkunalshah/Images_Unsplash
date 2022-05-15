package com.kunal.sunbase_task.data.network.apis

import com.kunal.sunbase_task.BuildConfig
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.data.network.models.PhotoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_CLIENT_ID
    ): Response<List<Photo>>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_CLIENT_ID
    ): Response<PhotoSearchResponse>
}