package com.avriansyah.githubuserapps.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: Favorite)

    @Query("DELETE FROM favorite_users WHERE login = :favoriteUser")
    fun delete(favoriteUser: String)

    @Query("SELECT * FROM favorite_users WHERE login = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<Favorite>

    @Query("SELECT * FROM favorite_users")
    fun getFavoriteUsers(): LiveData<List<Favorite>>
}