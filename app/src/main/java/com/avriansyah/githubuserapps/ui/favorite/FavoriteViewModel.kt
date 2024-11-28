package com.avriansyah.githubuserapps.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.avriansyah.githubuserapps.data.database.Favorite
import com.avriansyah.githubuserapps.data.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val userFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getFavoriteUsers(): LiveData<List<Favorite>> = userFavoriteRepository.getFavoriteUsers()

}