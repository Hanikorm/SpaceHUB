package com.hanikorm.spacehub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hanikorm.spacehub.databinding.ActivityFullScreenImageBinding

class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val imageResId = intent.getIntExtra(EXTRA_IMAGE_RES_ID, 0)

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.fullScreenImageView)
        } else if (imageResId != 0) {
            Glide.with(this)
                .load(imageResId)
                .into(binding.fullScreenImageView)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        const val EXTRA_IMAGE_RES_ID = "EXTRA_IMAGE_RES_ID"
    }
}
