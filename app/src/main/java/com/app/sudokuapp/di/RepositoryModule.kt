package com.app.sudokuapp.di

import com.app.sudokuapp.data.repository.SudokuRepositoryImpl
import com.app.sudokuapp.domain.repository.SudokuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun bindSudokuRepository(
        implementation: SudokuRepositoryImpl
    ): SudokuRepository {
        return implementation
    }
}