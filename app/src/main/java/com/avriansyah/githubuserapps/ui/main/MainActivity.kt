package com.avriansyah.githubuserapps.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avriansyah.githubuserapps.R
import com.avriansyah.githubuserapps.data.response.ItemsItem
import com.avriansyah.githubuserapps.databinding.ActivityMainBinding
import com.avriansyah.githubuserapps.ui.adabter.Adabter
import com.avriansyah.githubuserapps.ui.favorite.FavoriteActivity
import com.avriansyah.githubuserapps.ui.setting.SettingActivity
import com.avriansyah.githubuserapps.ui.setting.SettingPreferences
import com.avriansyah.githubuserapps.ui.setting.SettingViewModel
import com.avriansyah.githubuserapps.ui.setting.SettingViewModelFactory
import com.avriansyah.githubuserapps.ui.setting.dataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Github Search User"


        initTheme()

        mainViewModel.users.observe(this){user ->
            if (user != null)
                setUsers(user)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{ textView, actionId, event->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    mainViewModel.findUsers(searchView.text.toString())
                    false
                }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setUsers(users: ArrayList<ItemsItem>) {
        binding.rvUsers.adapter = Adabter(users)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
    }
    private fun initTheme() {
        val settingPreferences = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(settingPreferences))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

}