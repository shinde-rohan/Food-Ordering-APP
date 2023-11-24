package com.example.foodorderingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodorderingapp.Fragment.NotificationFragment
import com.example.foodorderingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        var NavController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(NavController)
        binding.notificationBtn.setOnClickListener {
            val bottomSheetDialog = NotificationFragment()
            bottomSheetDialog.show(supportFragmentManager,"test")
        }
    }
}