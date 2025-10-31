package com.hanikorm.spacehub

import android.util.Log
import androidx.lifecycle.*
import com.hanikorm.spacehub.model.Apod
import com.hanikorm.spacehub.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _apods = MutableLiveData<List<Apod>>()
    val apods: LiveData<List<Apod>> = _apods

    fun loadApods() {
        viewModelScope.launch {
            try {
                // Запрашиваем 50 объектов для создания галереи
                val response = RetrofitInstance.api.getApodList(BuildConfig.NASA_API_KEY, 50)
                _apods.value = response
            } catch (e: Exception) {
                Log.e("ApodData", "Ошибка при загрузке данных: ", e)
            }
        }
    }
}
