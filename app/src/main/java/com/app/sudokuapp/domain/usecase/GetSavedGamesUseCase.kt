package com.app.sudokuapp.domain.usecase

import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedGamesUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    operator fun invoke(onlyInProgress: Boolean = false): Flow<List<Sudoku>> {
        return if (onlyInProgress) {
            repository.fetchInProgressSudokus()
        } else {
            repository.fetchAllSudokus()
        }
    }

    suspend fun retrieveSudokuById(id: String): Sudoku? {
        return repository.getSudokuById(id)
    }
}