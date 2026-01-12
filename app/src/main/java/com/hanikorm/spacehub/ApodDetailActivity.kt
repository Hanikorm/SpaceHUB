package com.hanikorm.spacehub

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hanikorm.spacehub.databinding.ActivityApodDetailBinding
import com.hanikorm.spacehub.model.Apod

class ApodDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApodDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apod = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_APOD, Apod::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_APOD)
        }

        if (apod != null) {
            displayApodDetails(apod)
        }
    }

    private fun displayApodDetails(apod: Apod) {
        binding.detailTitleTextView.text = apod.title
        binding.detailDateTextView.text = apod.date
        binding.detailExplanationTextView.text = apod.explanation

        if (apod.mediaType == "image") {
            binding.detailImageView.visibility = View.VISIBLE
            binding.videoLinkTextView.visibility = View.GONE
            
            val imageUrl = apod.url
            if (imageUrl?.startsWith("http") == true) {
                Glide.with(this).load(imageUrl).into(binding.detailImageView)
                binding.detailImageView.setOnClickListener { 
                    openFullScreenImage(imageUrl, 0)
                }
            } else if (imageUrl != null) {
                val resourceId = resources.getIdentifier(imageUrl, "drawable", packageName)
                if (resourceId != 0) {
                    Glide.with(this).load(resourceId).into(binding.detailImageView)
                    binding.detailImageView.setOnClickListener { 
                        openFullScreenImage(null, resourceId)
                    }
                }
            }

        } else if (apod.mediaType == "video") {
            binding.detailImageView.visibility = View.GONE
            binding.videoLinkTextView.visibility = View.VISIBLE
            binding.videoLinkTextView.setOnClickListener {
                apod.url?.let { videoUrl -> 
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                    startActivity(intent)
                }
            }
        }
    }

    private fun openFullScreenImage(url: String?, resId: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java).apply {
            putExtra(FullScreenImageActivity.EXTRA_IMAGE_URL, url)
            putExtra(FullScreenImageActivity.EXTRA_IMAGE_RES_ID, resId)
        }
        startActivity(intent)
    }

    companion object {
        const val EXTRA_APOD = "EXTRA_APOD"
    }
}
