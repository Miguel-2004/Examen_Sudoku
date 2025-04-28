package com.app.sudokuapp.data.repository

import com.app.sudokuapp.data.api.SudokuApiService
import com.app.sudokuapp.data.cache.dao.SudokuDao
import com.app.sudokuapp.data.cache.entity.SudokuEntity
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import com.app.sudokuapp.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepositoryImpl @Inject constructor(
    private val apiService: SudokuApiService,
    private val sudokuDao: SudokuDao,
    private val apiKey: String
) : SudokuRepository {

    override suspend fun generateSudoku(size: SudokuSize, difficulty: SudokuDifficulty): Result<Sudoku> {
        return try {
            val dimension = size.dimension
            val apiResponse = apiService.fetchGeneratedSudoku(
                apiKey = apiKey,
                level = difficulty.apiValue,
                gridWidth = dimension / 2,
                gridHeight = dimension / 2
            )

            val newSudoku = Sudoku(
                id = UUID.randomUUID().toString(),
                puzzle = apiResponse.puzzleGrid,
                solution = apiResponse.solutionGrid,
                currentState = apiResponse.puzzleGrid.map { it.toMutableList() },
                size = size,
                difficulty = difficulty
            )

            saveSudoku(newSudoku)

            Result.success(newSudoku)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveSudoku(sudoku: Sudoku) {
        val entity = SudokuEntity(
            id = sudoku.id,
            initialPuzzle = sudoku.puzzle,
            solutionGrid = sudoku.solution,
            currentProgress = sudoku.currentState,
            gridSize = sudoku.size,
            gameDifficulty = sudoku.difficulty,
            timestamp = sudoku.timestamp,
            creationTimestamp = sudoku.timestamp,
            completed = sudoku.isCompleted
        )
        sudokuDao.insertSudoku(entity)
    }

    override suspend fun getSudokuById(id: String): Sudoku? {
        val entity = sudokuDao.fetchSudokuById(id) ?: return null
        return mapEntityToDomain(entity)
    }

    override fun fetchAllSudokus(): Flow<List<Sudoku>> {
        return sudokuDao.getAllSudokuGames().map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override fun fetchInProgressSudokus(): Flow<List<Sudoku>> {
        return sudokuDao.getOngoingSudokuGames().map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override fun fetchCompletedSudokus(): Flow<List<Sudoku>> {
        return sudokuDao.getFinishedSudokuGames().map { entities ->
            entities.map { mapEntityToDomain(it) }
        }
    }

    override suspend fun removeSudoku(id: String) {
        sudokuDao.removeSudoku(id)
    }

    override suspend fun refreshSudokuState(id: String, updatedState: List<List<Int?>>, isCompleted: Boolean) {
        val sudokuEntity = sudokuDao.fetchSudokuById(id) ?: return
        val updatedEntity = sudokuEntity.copy(
            initialPuzzle = sudokuEntity.initialPuzzle,
            solutionGrid = sudokuEntity.solutionGrid,
            currentProgress = updatedState,
            gridSize = sudokuEntity.gridSize,
            gameDifficulty = sudokuEntity.gameDifficulty,
            timestamp = sudokuEntity.timestamp,
            creationTimestamp = sudokuEntity.creationTimestamp,
            completed = isCompleted
        )
        sudokuDao.insertSudoku(updatedEntity)
    }

    private fun mapEntityToDomain(entity: SudokuEntity): Sudoku {
        return Sudoku(
            id = entity.id,
            puzzle = entity.initialPuzzle,
            solution = entity.solutionGrid,
            currentState = entity.currentProgress,
            size = entity.gridSize,
            difficulty = entity.gameDifficulty,
            timestamp = entity.timestamp,
            isCompleted = entity.completed
        )
    }
}