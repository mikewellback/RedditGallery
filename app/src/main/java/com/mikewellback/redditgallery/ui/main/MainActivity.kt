package com.mikewellback.redditgallery.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mikewellback.redditgallery.R
import com.mikewellback.redditgallery.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val abc = AppBarConfiguration(setOf(R.id.search_nav, R.id.favorites_nav))
        supportFragmentManager.findFragmentById(R.id.host_fra)?.findNavController()?.also {
            navController ->
            setupActionBarWithNavController(navController, abc)
            binding.navView.setupWithNavController(navController)
        }
    }
}