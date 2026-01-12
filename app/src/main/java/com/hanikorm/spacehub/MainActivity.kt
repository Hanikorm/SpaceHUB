package com.hanikorm.spacehub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hanikorm.spacehub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnApod.setOnClickListener {
            startActivity(Intent(this, ApodActivity::class.java))
        }

        binding.btnNeo.setOnClickListener {
            startActivity(Intent(this, NeoListActivity::class.java))
        }

        
    }
}
