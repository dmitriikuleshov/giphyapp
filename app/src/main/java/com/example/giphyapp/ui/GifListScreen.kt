package com.example.giphyapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.giphyapp.data.GifRepository


@Composable
fun GifListScreen(viewModel: GifListViewModel = viewModel()) {
    val gifs by viewModel.gifs.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // load more on scroll to bottom
    LaunchedEffect(listState) {
        while (true) {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            if (lastVisibleItem?.index == gifs.size - 1 &&
                gifs.isNotEmpty() &&
                !isLoading &&
                lastVisibleItem.index >= gifs.size - 5
            ) {
                viewModel.loadMore()
            }
            kotlinx.coroutines.delay(500)
        }
    }

    // initial load
    LaunchedEffect(Unit) {
        viewModel.loadGifs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            error != null && gifs.isEmpty() -> {
                ErrorRetryScreen(
                    error = error!!,
                    onRetry = { viewModel.retry() }
                )
            }

            gifs.isEmpty() && isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(gifs) { index, gif ->
                        GifCard(
                            gif = gif,
                            index = index + 1,
                            onClick = {
                            }
                        )
                    }
                }
            }
        }

        // pagination loader
        if (isLoading && gifs.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

class GifListViewModel : ViewModel() {
    private val repository = GifRepository()

    val gifs = repository.gifs
    val isLoading = repository.isLoading
    val error = repository.error

    fun loadGifs() {
        repository.loadGifs(isRefresh = true)
    }

    fun loadMore() {
        repository.loadGifs(isRefresh = false)
    }

    fun retry() {
        repository.retry()
    }
}
