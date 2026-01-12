package com.hanikorm.spacehub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hanikorm.spacehub.databinding.ActivityNeoListBinding
import com.hanikorm.spacehub.model.NeoObject
import com.hanikorm.spacehub.network.NetworkModule
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Calendar 
import java.util.Date
import java.util.Locale

class NeoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNeoListBinding
    private lateinit var neoAdapter: NeoAdapter
    private val api = NetworkModule.create()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchNeoFeedForWeek()
    }

    private fun setupRecyclerView() {
        neoAdapter = NeoAdapter { neo ->
            showNeoDetailsDialog(neo)
        }
        binding.recyclerView.adapter = neoAdapter
    }

    private fun fetchNeoFeedForWeek() {
        scope.launch {
            showLoading(true)
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val calendar = Calendar.getInstance()
                val today = dateFormat.format(calendar.time)
                
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val weekLater = dateFormat.format(calendar.time)

                val response = withContext(Dispatchers.IO) {
                    api.getNeoFeed(today, weekLater, BuildConfig.NASA_API_KEY)
                }

                if (response.isSuccessful && response.body() != null) {
                    val feed = response.body()!!
                    
                    val allNeos = feed.nearEarthObjects.values.flatten()

                    // ИСПРАВЛЕНО: Сортировка по реальной дате, а не по выдуманному полю
                    val sortedNeos = allNeos.sortedBy {
                        try {
                            val date = it.closeApproachData.firstOrNull()?.closeApproachDate
                            if (date != null) dateFormat.parse(date) else Date(0)
                        } catch (e: Exception) {
                            Date(0)
                        }
                    }

                    if (sortedNeos.isNotEmpty()){
                        neoAdapter.submitList(sortedNeos)
                        showError(false)
                    } else {
                        showError(true, "В базе нет объектов на ближайшую неделю.")
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

    private fun showNeoDetailsDialog(neo: NeoObject) {
        val approachDate = neo.closeApproachData.firstOrNull()?.closeApproachDate ?: "N/A"

        val details = "Имя: ${neo.name}\n"
            .plus("ID: ${neo.id}\n")
            .plus("Дата сближения: $approachDate\n") 
            .plus("Потенциально опасен: ${if(neo.isPotentiallyHazardous) "Да" else "Нет"}\n")
            .plus("Абсолютная звездная величина: ${neo.absoluteMagnitudeH}\n")

        AlertDialog.Builder(this)
            .setTitle("Подробности об объекте")
            .setMessage(details)
            .setPositiveButton("Закрыть", null)
            .show()
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
