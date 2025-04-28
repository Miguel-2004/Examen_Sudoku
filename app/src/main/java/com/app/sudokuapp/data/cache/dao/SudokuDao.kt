package com.app.sudokuapp.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.sudokuapp.data.cache.entity.SudokuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SudokuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSudoku(sudokuEntity: SudokuEntity)

    @Query("SELECT * FROM sudoku_games WHERE id = :gameId")
    suspend fun fetchSudokuById(gameId: String): SudokuEntity?

    @Query("SELECT * FROM sudoku_games ORDER BY timestamp DESC")
    fun getAllSudokuGames(): Flow<List<SudokuEntity>>

    @Query("SELECT * FROM sudoku_games WHERE completed = 0 ORDER BY timestamp DESC")
    fun getOngoingSudokuGames(): Flow<List<SudokuEntity>>

    @Query("SELECT * FROM sudoku_games WHERE completed = 1 ORDER BY timestamp DESC")
    fun getFinishedSudokuGames(): Flow<List<SudokuEntity>>

    @Query("DELETE FROM sudoku_games WHERE id = :gameId")
    suspend fun removeSudoku(gameId: String)
}