package com.example.giphyapp.data

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.giphyapp.BuildConfig

interface GiphyApiService {
    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String = BuildConfig.GIPHY_API_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): GiphyResponse
}
