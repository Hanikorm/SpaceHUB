package com.hanikorm.spacehub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hanikorm.spacehub.databinding.ActivityMarsPhotosBinding
import com.hanikorm.spacehub.network.NetworkModule
import kotlinx.coroutines.*

class MarsPhotosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarsPhotosBinding
    private lateinit var marsPhotosAdapter: MarsPhotosAdapter
    private val api = NetworkModule.create()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarsPhotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchMarsPhotos()
    }

    private fun setupRecyclerView() {
        marsPhotosAdapter = MarsPhotosAdapter()
        binding.recyclerView.adapter = marsPhotosAdapter
    }

    private fun fetchMarsPhotos() {
        scope.launch {
            showLoading(true)
            try {
                // ИЗМЕНЕНИЕ: Используем earth_date вместо sol для 100% результата
                val response = withContext(Dispatchers.IO) {
                    api.getMarsRoverPhotos(
                        rover = "curiosity", 
                        earthDate = "2021-09-29", // Гарантированно рабочий день
                        apiKey = BuildConfig.NASA_API_KEY
                    )
                }

                if (response.isSuccessful && response.body() != null) {
                    val photos = response.body()!!.photos
                    if (photos.isNotEmpty()) {
                        marsPhotosAdapter.submitList(photos)
                        showError(false)
                    } else {
                        showError(true, "Фотографий не найдено.")
                    }
                } else {
                    showError(true, "Ошибка: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                showError(true, "Исключение: ${e.localizedMessage}")
            }
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(isError: Boolean, message: String = "") {
        binding.errorTextView.visibility = if (isError) View.VISIBLE else View.GONE
        if (isError) {
            binding.errorTextView.text = message
            binding.recyclerView.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
