package com.hanikorm.spacehub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanikorm.spacehub.databinding.ActivityApodBinding
import com.hanikorm.spacehub.model.Apod
import java.io.IOException

class ApodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApodBinding
    private lateinit var apodAdapter: ApodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadApodData()
    }

    private fun setupRecyclerView() {
        apodAdapter = ApodAdapter()
        binding.recyclerView.adapter = apodAdapter

        apodAdapter.onItemClick = { apod ->
            val intent = Intent(this, ApodDetailActivity::class.java).apply {
                putExtra(ApodDetailActivity.EXTRA_APOD, apod)
            }
            startActivity(intent)
        }
    }

    // ИСПРАВЛЕНО: Функция теперь читает данные из файла apod_offline.json
    private fun loadApodData() {
        showLoading(true)
        try {
            val jsonString = applicationContext.assets.open("apod_offline.json").bufferedReader().use {
                it.readText()
            }
            val listType = object : TypeToken<List<Apod>>() {}.type
            val apodList: List<Apod> = Gson().fromJson(jsonString, listType)
            apodAdapter.submitList(apodList.reversed()) // Показываем сначала новые
            showError(false)
        } catch (e: IOException) {
            showError(true, "Ошибка чтения файла: ${e.localizedMessage}")
        } catch (e: Exception) {
            showError(true, "Не удалось обработать данные: ${e.localizedMessage}")
        }
        showLoading(false)
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
}
