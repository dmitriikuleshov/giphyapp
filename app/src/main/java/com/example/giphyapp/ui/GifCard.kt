package com.example.giphyapp.ui

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.decode.GifDecoder
import com.example.giphyapp.data.GifData

@Composable
fun GifCard(
    gif: GifData,
    index: Int,
    onClick: (GifData) -> Unit,
    modifier: Modifier = Modifier
) {
    // pinterest стиль случайная высота
    val randomHeight = (250..400).random().dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(randomHeight)
            .clickable { onClick(gif) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(gif.images.fixed_height.url)
                    .decoderFactory(GifDecoder.Factory())
                    .crossfade(500)
                    .build(),
                contentDescription = gif.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "#$index ${gif.title}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}
