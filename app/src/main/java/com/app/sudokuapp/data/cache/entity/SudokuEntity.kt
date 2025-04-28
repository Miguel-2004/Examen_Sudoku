package com.app.sudokuapp.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "sudoku_games")
@TypeConverters(SudokuConverters::class)
data class SudokuEntity(
    @PrimaryKey val id: String,
    val initialPuzzle: List<List<Int?>>,
    val solutionGrid: List<List<Int>>,
    val currentProgress: List<List<Int?>>,
    val gridSize: SudokuSize,
    val gameDifficulty: SudokuDifficulty,
    val timestamp: Long,
    val creationTimestamp: Long,
    val completed: Boolean
)

class SudokuConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromNullableIntMatrix(matrix: List<List<Int?>>): String {
        return gson.toJson(matrix)
    }

    @TypeConverter
    fun toNullableIntMatrix(json: String): List<List<Int?>> {
        val type = object : TypeToken<List<List<Int?>>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromIntMatrix(matrix: List<List<Int>>): String {
        return gson.toJson(matrix)
    }

    @TypeConverter
    fun toIntMatrix(json: String): List<List<Int>> {
        val type = object : TypeToken<List<List<Int>>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromSize(size: SudokuSize): String {
        return size.name
    }

    @TypeConverter
    fun toSize(value: String): SudokuSize {
        return SudokuSize.valueOf(value)
    }

    @TypeConverter
    fun fromDifficulty(difficulty: SudokuDifficulty): String {
        return difficulty.name
    }

    @TypeConverter
    fun toDifficulty(value: String): SudokuDifficulty {
        return SudokuDifficulty.valueOf(value)
    }
}