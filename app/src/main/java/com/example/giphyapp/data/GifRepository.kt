package com.example.giphyapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GifRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.giphy.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GiphyApiService::class.java)

    private val _gifs = MutableStateFlow<List<GifData>>(emptyList())
    val gifs: StateFlow<List<GifData>> = _gifs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentOffset = 0
    private val cache = mutableListOf<GifData>()

    fun loadGifs(isRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = api.getTrendingGifs(offset = if (isRefresh) 0 else currentOffset)

                val newGifs = response.data
                if (isRefresh) {
                    cache.clear()
                    cache.addAll(newGifs)
                    _gifs.value = cache.toList()
                    currentOffset = newGifs.size
                } else {
                    cache.addAll(newGifs)
                    _gifs.value = cache.toList()
                    currentOffset += newGifs.size
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry() {
        currentOffset = 0
        loadGifs(isRefresh = true)
    }
}
