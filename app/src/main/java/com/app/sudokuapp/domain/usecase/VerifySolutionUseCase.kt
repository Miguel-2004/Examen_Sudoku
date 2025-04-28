package com.app.sudokuapp.domain.usecase

import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.isFullyCompleted

class VerifySolutionUseCase {
    operator fun invoke(sudoku: Sudoku): Boolean {
        return sudoku.isFullyCompleted()
    }

    fun validateSingleCell(sudoku: Sudoku, rowIndex: Int, colIndex: Int): Boolean {
        val value = sudoku.currentState[rowIndex][colIndex] ?: return true

        // Verificar fila
        for (c in sudoku.currentState[rowIndex].indices) {
            if (c != colIndex && sudoku.currentState[rowIndex][c] == value) {
                return false
            }
        }

        // Verificar columna
        for (r in sudoku.currentState.indices) {
            if (r != rowIndex && sudoku.currentState[r][colIndex] == value) {
                return false
            }
        }

        // Verificar cuadrante
        val boxSize = when (sudoku.size.dimension) {
            4 -> 2
            9 -> 3
            16 -> 4
            else -> throw IllegalArgumentException("Unsupported sudoku size: ${sudoku.size}")
        }

        val startRow = (rowIndex / boxSize) * boxSize
        val startCol = (colIndex / boxSize) * boxSize

        for (r in startRow until startRow + boxSize) {
            for (c in startCol until startCol + boxSize) {
                if (r != rowIndex && c != colIndex && sudoku.currentState[r][c] == value) {
                    return false
                }
            }
        }

        return true
    }
}