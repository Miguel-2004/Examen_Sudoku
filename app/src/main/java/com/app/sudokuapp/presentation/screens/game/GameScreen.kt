package com.app.sudokuapp.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.SudokuCell
import com.app.sudokuapp.presentation.components.SudokuGameBoard
import com.app.sudokuapp.presentation.components.SudokuNumberPad
import com.app.sudokuapp.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit
) {
    val sudokuState by viewModel.sudokuState.collectAsState()
    val selectedCell by viewModel.selectedCell.collectAsState()
    val isSolved by viewModel.isSolved.collectAsState()
    val isNoteMode by viewModel.isNoteMode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sudoku Challenge", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (sudokuState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val sudoku = (sudokuState as UiState.Success<Sudoku>).result
                    GameContentDisplay(
                        sudoku = sudoku,
                        selectedCell = selectedCell,
                        isNoteMode = isNoteMode,
                        isSolved = isSolved,
                        onCellTapped = { row, col -> viewModel.selectCell(row, col) },
                        onNumberSelected = { num -> viewModel.setValueForSelectedCell(num) },
                        onClearTapped = { viewModel.setValueForSelectedCell(null) },
                        onNoteNumberTapped = { num -> viewModel.toggleNote(num) },
                        onVerifySolutionTapped = { viewModel.verifySolution() },
                        onToggleNotesTapped = { viewModel.toggleNoteMode() },
                        onResetTapped = { viewModel.resetGame() }
                    )
                }
                is UiState.Error -> {
                    Text(
                        text = "Error al cargar el Sudoku",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun GameContentDisplay(
    sudoku: Sudoku,
    selectedCell: SudokuCell?,
    isNoteMode: Boolean,
    isSolved: Boolean,
    onCellTapped: (Int, Int) -> Unit,
    onNumberSelected: (Int) -> Unit,
    onClearTapped: () -> Unit,
    onNoteNumberTapped: (Int) -> Unit,
    onVerifySolutionTapped: () -> Unit,
    onToggleNotesTapped: () -> Unit,
    onResetTapped: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Información del juego
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${sudoku.size.dimension}x${sudoku.size.dimension} - ${sudoku.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleMedium
                )
                if (isSolved) {
                    Text(
                        text = "🎉 ¡Sudoku completado!",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Tablero de Sudoku
        SudokuGameBoard(
            sudoku = sudoku,
            selectedCell = selectedCell,
            onCellTapped = onCellTapped,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        // Barra de acciones rápidas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onToggleNotesTapped) {
                Icon(Icons.Default.Edit, contentDescription = "Notas")
            }
            IconButton(onClick = onVerifySolutionTapped) {
                Icon(Icons.Default.Check, contentDescription = "Verificar")
            }
            IconButton(onClick = onResetTapped) {
                Icon(Icons.Default.Refresh, contentDescription = "Resetear")
            }
        }

        // Pad de números
        SudokuNumberPad(
            sudokuSize = sudoku.size,
            onNumberSelected = onNumberSelected,
            onClearSelected = onClearTapped,
            onNoteSelected = onNoteNumberTapped,
            isNoteModeActive = isNoteMode,
            modifier = Modifier.fillMaxWidth(0.85f)
        )
    }
}
