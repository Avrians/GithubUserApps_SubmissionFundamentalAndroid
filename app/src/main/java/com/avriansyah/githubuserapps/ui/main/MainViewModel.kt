package com.avriansyah.githubuserapps.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avriansyah.githubuserapps.data.response.ItemsItem
import com.avriansyah.githubuserapps.data.response.ResponseUsers
import com.avriansyah.githubuserapps.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _users = MutableLiveData<ArrayList<ItemsItem>?>()
    val users: MutableLiveData<ArrayList<ItemsItem>?> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        getUsers()
    }
    private fun getUsers(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>
            ) {

                _isLoading.value=false
                if (response.isSuccessful){
                    val responseBody = response.body()

                    if (responseBody != null)
                        _users.value=responseBody
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                _isLoading.value=false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })

    }
    fun findUsers(query: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().findUsers(query)

        client.enqueue(object : Callback<ResponseUsers> {
            override fun onResponse(
                call: Call<ResponseUsers>,
                response: Response<ResponseUsers>
            ) {
                _isLoading.value=false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody.items
                    }
                }
            }
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                _isLoading.value=false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}