package com.avriansyah.githubuserapps.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.avriansyah.githubuserapps.data.database.Favorite
import com.avriansyah.githubuserapps.data.database.FavoriteDao
import com.avriansyah.githubuserapps.data.database.FavoriteRoom
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val userFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoom.getDatabase(application)

        userFavoriteDao = db.userFavoriteDao()
    }

    fun insert(favoriteUser: Favorite) {
        executorService.execute { userFavoriteDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: String) {
        executorService.execute { userFavoriteDao.delete(favoriteUser) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<Favorite> =
        userFavoriteDao.getFavoriteUserByUsername(username)

    fun getFavoriteUsers() = userFavoriteDao.getFavoriteUsers()

}