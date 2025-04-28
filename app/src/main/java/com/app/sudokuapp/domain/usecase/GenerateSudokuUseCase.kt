package com.app.sudokuapp.domain.usecase

import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import com.app.sudokuapp.domain.repository.SudokuRepository
import javax.inject.Inject

class GenerateSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(size: SudokuSize, difficulty: SudokuDifficulty): Result<Sudoku> {
        return repository.generateSudoku(size, difficulty)
    }
}