package com.kunal.sunbase_task.di

import android.content.Context
import com.kunal.sunbase_task.data.network.NetworkConnectionInterceptor
import com.kunal.sunbase_task.data.network.apis.ImagesApi
import com.kunal.sunbase_task.data.network.ConnectionLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideConnectionLiveData(@ApplicationContext context: Context): ConnectionLiveData {
        return ConnectionLiveData(context)
    }

    @Singleton
    @Provides
    fun provideImagesApiService(
        interceptor: NetworkConnectionInterceptor
    ): ImagesApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImagesApi::class.java)
    }

}