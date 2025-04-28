package com.app.sudokuapp.di

import android.content.Context
import com.app.sudokuapp.data.cache.SudokuDatabase
import com.app.sudokuapp.data.cache.dao.SudokuDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun createSudokuDatabase(@ApplicationContext context: Context): SudokuDatabase {
        return SudokuDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun createSudokuDao(database: SudokuDatabase): SudokuDao {
        return database.provideSudokuDao()
    }
}