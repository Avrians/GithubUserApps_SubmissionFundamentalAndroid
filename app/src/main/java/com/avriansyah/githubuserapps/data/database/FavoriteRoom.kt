package com.avriansyah.githubuserapps.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 2)
abstract class FavoriteRoom : RoomDatabase() {
    abstract fun userFavoriteDao(): FavoriteDao
    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoom? = null
        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoom {
            if (INSTANCE == null) {
                synchronized(FavoriteRoom::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteRoom::class.java, "users_favorite")
                        .build()
                }
            }
            return INSTANCE as FavoriteRoom
        }
    }
}