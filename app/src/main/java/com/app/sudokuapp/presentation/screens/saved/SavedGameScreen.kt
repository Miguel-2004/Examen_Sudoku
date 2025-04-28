package com.app.sudokuapp.presentation.screens.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.presentation.util.UiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGamesScreen(
    viewModel: SavedGamesViewModel,
    onNavigateBack: () -> Unit,
    onGameSelected: (String) -> Unit
) {
    val savedGamesState by viewModel.savedGamesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tus Retos Guardados", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (savedGamesState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val successState = savedGamesState as UiState.Success
                    val games = successState.result
                    if (games.isEmpty()) {
                        Text(
                            text = "¡Todavía no tienes partidas guardadas!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(games.size) { index ->
                                SavedGameCard(
                                    sudoku = games[index],
                                    onGameClick = { onGameSelected(games[index].id) }
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = "No pudimos cargar tus partidas. Inténtalo más tarde.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun SavedGameCard(
    sudoku: Sudoku,
    onGameClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = onGameClick
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${sudoku.size.dimension}x${sudoku.size.dimension} - ${sudoku.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Guardado el: ${formatDate(sudoku.timestamp)}",
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(
                onClick = onGameClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Continue")
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
    return format.format(date)
}
