package com.example.giphyapp.data

data class GiphyResponse(
    val data: List<GifData>,
    val pagination: Pagination
)

data class GifData(
    val id: String,
    val images: Images,
    val title: String
)

data class Images(
    val original: GifImage,
    val fixed_height: GifImage
)

data class GifImage(
    val url: String,
    val width: String,
    val height: String
)

data class Pagination(
    val total_count: Int,
    val count: Int,
    val offset: Int
)
