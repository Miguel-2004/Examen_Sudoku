package com.app.sudokuapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.SudokuCell
import com.app.sudokuapp.domain.model.SudokuSize
import com.app.sudokuapp.domain.model.findCell

@Composable
fun SudokuGameBoard(
    sudoku: Sudoku,
    selectedCell: SudokuCell?,
    onCellTapped: (rowIndex: Int, colIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimension = sudoku.size.dimension
    val boxSize = when (sudoku.size) {
        SudokuSize.SMALL -> 2
        SudokuSize.STANDARD -> 3
    }

    val thinBorder = 0.5.dp
    val thickBorder = 2.dp

    Column(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
            .border(thickBorder, MaterialTheme.colorScheme.outline)
            .background(MaterialTheme.colorScheme.background)
            .padding(2.dp)
    ) {
        for (rowIndex in 0 until dimension) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                for (colIndex in 0 until dimension) {
                    val cell = sudoku.findCell(rowIndex, colIndex)
                    val isSelected = selectedCell?.rowIndex == rowIndex && selectedCell.colIndex == colIndex

                    val isThickRight = (colIndex + 1) % boxSize == 0 && colIndex < dimension - 1
                    val isThickBottom = (rowIndex + 1) % boxSize == 0 && rowIndex < dimension - 1

                    val boxRow = rowIndex / boxSize
                    val boxCol = colIndex / boxSize
                    val boxIndex = boxRow * boxSize + boxCol
                    val isEvenBox = boxIndex % 2 == 0

                    val backgroundShade = if (isEvenBox) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(backgroundShade)
                            .border(
                                width = if (isThickRight) thickBorder else thinBorder,
                                color = MaterialTheme.colorScheme.outline.copy(
                                    alpha = if (isThickRight) 0.7f else 0.3f
                                ),
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .border(
                                width = if (isThickBottom) thickBorder else thinBorder,
                                color = MaterialTheme.colorScheme.outline.copy(
                                    alpha = if (isThickBottom) 0.7f else 0.3f
                                ),
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(1.dp)
                    ) {
                        SudokuCellComponent(
                            cell = cell,
                            isSelected = isSelected,
                            onCellClick = { onCellTapped(rowIndex, colIndex) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}