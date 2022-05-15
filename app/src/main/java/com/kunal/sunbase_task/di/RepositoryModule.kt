package com.kunal.sunbase_task.di

import com.kunal.sunbase_task.data.network.apis.ImagesApi
import com.kunal.sunbase_task.data.repositories.ImageRepository
import com.kunal.sunbase_task.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideImagesRepository(
        api: ImagesApi,
        appDatabase: AppDatabase
    ): ImageRepository {
        return ImageRepository(api,appDatabase)
    }
}