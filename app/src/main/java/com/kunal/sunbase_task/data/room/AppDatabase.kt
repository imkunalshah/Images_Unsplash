package com.kunal.sunbase_task.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.data.network.models.RemoteKeys
import com.kunal.sunbase_task.data.room.dao.PhotosDao
import com.kunal.sunbase_task.data.room.dao.RemoteKeysDao
import com.kunal.sunbase_task.data.room.type_converters.PhotoUrlsConverter

@Database(
    entities = [Photo::class, RemoteKeys::class],
    version = 1,
)
@TypeConverters(PhotoUrlsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPhotosDao(): PhotosDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "sunbaseapp"
            ).fallbackToDestructiveMigration()
                .build()
    }
}