package com.app.sudokuapp.domain.usecase

import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.repository.SudokuRepository
import javax.inject.Inject

class SaveGameUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(sudoku: Sudoku) {
        repository.saveSudoku(sudoku)
    }

    suspend fun updateCurrentGame(id: String, updatedState: List<List<Int?>>, isCompleted: Boolean) {
        repository.refreshSudokuState(id, updatedState, isCompleted)
    }

    suspend fun removeSavedGame(id: String) {
        repository.removeSudoku(id)
    }
}