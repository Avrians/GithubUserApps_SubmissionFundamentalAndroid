package com.avriansyah.githubuserapps.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.avriansyah.githubuserapps.R
import com.avriansyah.githubuserapps.data.database.Favorite
import com.avriansyah.githubuserapps.data.response.DetailResponse
import com.avriansyah.githubuserapps.data.response.ItemsItem
import com.avriansyah.githubuserapps.databinding.ActivityDetailBinding
import com.avriansyah.githubuserapps.ui.adabter.SectionsPagerAdapter
import com.avriansyah.githubuserapps.ui.favorite.FavoriteActivity
import com.avriansyah.githubuserapps.ui.favorite.FavoriteViewModelFactory
import com.avriansyah.githubuserapps.ui.setting.SettingActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        FavoriteViewModelFactory.getInstance(application)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val users = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ItemsItem>(EXTRA_USER, ItemsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ItemsItem>(EXTRA_USER)
        }
        if (users != null) {
            initObserver(users)
            initViewPager(users)
        }
    }
    private fun initViewPager(user : ItemsItem){
        val sectionsPagerAdapter = SectionsPagerAdapter(this,user.login)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter=sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs,viewPager) {tab, position ->
            tab.text= resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

    }
    private fun initObserver(user: ItemsItem) {
        detailViewModel.getUser(user.login)
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.user.observe(this) {
            if (it != null) {
                setUserData(it)
                setUserFavorite(it)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserData(user: DetailResponse) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .into(imgUserDetail)
            tvNameDetail.text=user.name
            tvUsernameDetail.text=user.login
            tvFollowers.text=user.followers.toString()
            tvFollowing.text=user.following.toString()
            tvRepository.text=user.publicRepos.toString()
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
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    private fun setUserFavorite(user: DetailResponse) {
        detailViewModel.getFavoriteUserByUsername(user.login ?: "").observe(this) {
            val favoriteUser = Favorite(user.login ?: "", user.avatarUrl)
            var isFavorite = false

            if (it != null) {
                isFavorite = true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
            }

            binding.fabFavorite.setOnClickListener {
                if (!isFavorite) {
                    detailViewModel.insert(favoriteUser)
                    isFavorite = true
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    detailViewModel.delete(favoriteUser.login)
                    isFavorite = false
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }
    }
}
