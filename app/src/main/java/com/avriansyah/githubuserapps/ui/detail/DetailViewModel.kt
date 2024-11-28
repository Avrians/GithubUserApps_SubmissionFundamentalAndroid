package com.avriansyah.githubuserapps.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avriansyah.githubuserapps.data.database.Favorite
import com.avriansyah.githubuserapps.data.repository.FavoriteRepository
import com.avriansyah.githubuserapps.data.response.DetailResponse
import com.avriansyah.githubuserapps.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val userFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _user = MutableLiveData<DetailResponse?>()
    val user: MutableLiveData<DetailResponse?> = _user


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getUser(username: String) {

        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _user.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    fun insert(userFavorite: Favorite) {
        userFavoriteRepository.insert(userFavorite)
    }

    fun delete(userFavorite: String) {
        userFavoriteRepository.delete(userFavorite)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<Favorite> =
        userFavoriteRepository.getFavoriteUserByUsername(username)

}