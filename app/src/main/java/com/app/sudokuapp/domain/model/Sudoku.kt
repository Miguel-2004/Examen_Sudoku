package com.app.sudokuapp.domain.model

enum class SudokuSize(val dimension: Int) {
    SMALL(4),
    STANDARD(9)
}

enum class SudokuDifficulty(val apiValue: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard")
}

data class Sudoku(
    val id: String = "",
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int>>,
    val currentState: List<List<Int?>>,
    val size: SudokuSize,
    val difficulty: SudokuDifficulty,
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)

data class SudokuCell(
    val rowIndex: Int,
    val colIndex: Int,
    val value: Int?,
    val isOriginal: Boolean,
    val isValid: Boolean = true,
    val notes: Set<Int> = emptySet()
)

// Función de extensión para obtener una celda
fun Sudoku.findCell(row: Int, col: Int): SudokuCell {
    val value = currentState[row][col]
    val isOriginal = puzzle[row][col] != null
    val isValid = verifyCell(row, col)
    return SudokuCell(row, col, value, isOriginal, isValid)
}

// Función de extensión para verificar si una celda es válida
fun Sudoku.verifyCell(row: Int, col: Int): Boolean {
    val value = currentState[row][col] ?: return true

    // Verificar fila
    for (c in currentState[row].indices) {
        if (c != col && currentState[row][c] == value) {
            return false
        }
    }

    // Verificar columna
    for (r in currentState.indices) {
        if (r != row && currentState[r][col] == value) {
            return false
        }
    }

    // Verificar cuadrante
    val boxSize = when (size) {
        SudokuSize.SMALL -> 2
        SudokuSize.STANDARD -> 3
    }

    val startRow = (row / boxSize) * boxSize
    val startCol = (col / boxSize) * boxSize

    for (r in startRow until startRow + boxSize) {
        for (c in startCol until startCol + boxSize) {
            if (r != row && c != col && currentState[r][c] == value) {
                return false
            }
        }
    }

    return true
}

// Función de extensión para comprobar si el Sudoku está completo
fun Sudoku.isFullyCompleted(): Boolean {
    for (row in currentState) {
        for (cell in row) {
            if (cell == null) return false
        }
    }

    for (rowIndex in currentState.indices) {
        for (colIndex in currentState[rowIndex].indices) {
            if (!verifyCell(rowIndex, colIndex)) {
                return false
            }
        }
    }

    return true
}