package com.app.sudokuapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.sudokuapp.domain.model.SudokuCell

@Composable
fun SingleSudokuCell(
    cell: SudokuCell,
    isCurrentlySelected: Boolean,
    onCellTapped: (SudokuCell) -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isCurrentlySelected -> MaterialTheme.colorScheme.primaryContainer
        cell.isOriginal -> MaterialTheme.colorScheme.surfaceVariant
        !cell.isValid -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surface
    }

    val numberColor = when {
        !cell.isValid -> MaterialTheme.colorScheme.error
        cell.isOriginal -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }

    val borderLineColor = when {
        isCurrentlySelected -> MaterialTheme.colorScheme.primary
        !cell.isValid -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(bgColor)
            .border(
                width = if (isCurrentlySelected) 2.dp else 1.dp,
                color = borderLineColor,
                shape = MaterialTheme.shapes.extraSmall
            )
            .clickable(enabled = !cell.isOriginal) { onCellTapped(cell) }
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (cell.value != null) {
                val fontSize = when {
                    cell.value < 10 -> 18.sp
                    else -> 14.sp
                }

                Text(
                    text = cell.value.toString(),
                    color = numberColor,
                    fontSize = fontSize,
                    fontWeight = if (cell.isOriginal) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            } else if (cell.notes.isNotEmpty()) {
                NotesGrid(notes = cell.notes, maxNumber = 16)
            }
        }
    }
}

@Composable
fun NotesGrid(notes: Set<Int>, maxNumber: Int) {
    val gridSize = when {
        maxNumber <= 4 -> 2
        maxNumber <= 9 -> 3
        else -> 4
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until gridSize) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0 until gridSize) {
                    val noteNumber = row * gridSize + col + 1
                    if (noteNumber <= maxNumber) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (notes.contains(noteNumber)) {
                                val noteFontSize = if (maxNumber <= 9) 8.sp else 6.sp

                                Text(
                                    text = noteNumber.toString(),
                                    fontSize = noteFontSize,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuCellComponent(
    cell: SudokuCell,
    isSelected: Boolean,
    onCellClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onCellClick() }
    ) {
        Text(
            text = cell.value?.toString() ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}