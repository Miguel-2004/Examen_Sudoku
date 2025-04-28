package com.app.sudokuapp.domain.repository

import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import kotlinx.coroutines.flow.Flow

interface SudokuRepository {
    suspend fun generateSudoku(size: SudokuSize, difficulty: SudokuDifficulty): Result<Sudoku>
    suspend fun saveSudoku(sudoku: Sudoku)
    suspend fun getSudokuById(id: String): Sudoku?
    fun fetchAllSudokus(): Flow<List<Sudoku>>
    fun fetchInProgressSudokus(): Flow<List<Sudoku>>
    fun fetchCompletedSudokus(): Flow<List<Sudoku>>
    suspend fun removeSudoku(id: String)
    suspend fun refreshSudokuState(id: String, updatedState: List<List<Int?>>, isCompleted: Boolean)
}