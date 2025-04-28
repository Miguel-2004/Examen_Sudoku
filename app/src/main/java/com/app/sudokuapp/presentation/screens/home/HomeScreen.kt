package com.app.sudokuapp.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import com.app.sudokuapp.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onStartNewGame: (SudokuSize, SudokuDifficulty) -> Unit,
    onContinueGame: (String) -> Unit
) {
    var selectedSize by remember { mutableStateOf(SudokuSize.STANDARD) }
    var selectedDifficulty by remember { mutableStateOf(SudokuDifficulty.EASY) }
    val savedGamesState by viewModel.savedGamesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sudoku Adventure", style = MaterialTheme.typography.headlineMedium) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿Listo para un nuevo reto?",
                style = MaterialTheme.typography.titleLarge
            )

            NewGameCard(
                selectedSize = selectedSize,
                selectedDifficulty = selectedDifficulty,
                onSizeSelected = { selectedSize = it },
                onDifficultySelected = { selectedDifficulty = it },
                onStartNewGame = { onStartNewGame(selectedSize, selectedDifficulty) }
            )

            ContinueGamesCard(savedGamesState, onContinueGame)
        }
    }
}

@Composable
fun NewGameCard(
    selectedSize: SudokuSize,
    selectedDifficulty: SudokuDifficulty,
    onSizeSelected: (SudokuSize) -> Unit,
    onDifficultySelected: (SudokuDifficulty) -> Unit,
    onStartNewGame: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Configuración:", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SudokuSize.values().forEach { size ->
                    FilterChip(
                        onClick = { onSizeSelected(size) },
                        label = { Text("${size.dimension}x${size.dimension}") },
                        selected = size == selectedSize
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SudokuDifficulty.values().forEach { diff ->
                    FilterChip(
                        onClick = { onDifficultySelected(diff) },
                        label = { Text(diff.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        selected = diff == selectedDifficulty
                    )
                }
            }

            Button(
                onClick = onStartNewGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "New Game")
                Spacer(Modifier.width(8.dp))
                Text("¡Comenzar Aventura!")
            }
        }
    }
}

@Composable
fun ContinueGamesCard(
    savedGamesState: UiState<List<com.app.sudokuapp.domain.model.Sudoku>>,
    onContinueGame: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tus partidas guardadas:", style = MaterialTheme.typography.titleMedium)

            when (savedGamesState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Success -> {
                    val games = savedGamesState.result
                    if (games.isEmpty()) {
                        Text("No tienes partidas guardadas.")
                    } else {
                        games.forEach { game ->
                            Button(
                                onClick = { onContinueGame(game.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Icon(Icons.Default.Save, contentDescription = "Saved Game")
                                Spacer(Modifier.width(8.dp))
                                Text("Continuar partida")
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text("Error al cargar partidas guardadas.")
                }
            }
        }
    }
}
