package com.app.sudokuapp.di

import com.app.sudokuapp.domain.repository.SudokuRepository
import com.app.sudokuapp.domain.usecase.GenerateSudokuUseCase
import com.app.sudokuapp.domain.usecase.GetSavedGamesUseCase
import com.app.sudokuapp.domain.usecase.SaveGameUseCase
import com.app.sudokuapp.domain.usecase.VerifySolutionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGeneratorUseCase(repository: SudokuRepository): GenerateSudokuUseCase {
        return GenerateSudokuUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRetrieverUseCase(repository: SudokuRepository): GetSavedGamesUseCase {
        return GetSavedGamesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaverUseCase(repository: SudokuRepository): SaveGameUseCase {
        return SaveGameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideVerifierUseCase(): VerifySolutionUseCase {
        return VerifySolutionUseCase()
    }
}