package com.app.sudokuapp.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.sudokuapp.data.cache.dao.SudokuDao
import com.app.sudokuapp.data.cache.entity.SudokuEntity
import com.app.sudokuapp.data.cache.entity.SudokuConverters

@Database(entities = [SudokuEntity::class], version = 1, exportSchema = false)
@TypeConverters(SudokuConverters::class)
abstract class SudokuDatabase : RoomDatabase() {

    abstract fun provideSudokuDao(): SudokuDao

    companion object {
        @Volatile
        private var INSTANCE: SudokuDatabase? = null

        fun getInstance(context: Context): SudokuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SudokuDatabase::class.java,
                    "sudoku_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}