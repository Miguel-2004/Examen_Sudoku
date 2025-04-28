package com.app.sudokuapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.usecase.GetSavedGamesUseCase
import com.app.sudokuapp.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val retrieverUseCase: GetSavedGamesUseCase
) : ViewModel() {

    private val _savedGamesState = MutableStateFlow<UiState<List<Sudoku>>>(UiState.Loading)
    val savedGamesState: StateFlow<UiState<List<Sudoku>>> = _savedGamesState.asStateFlow()

    init {
        loadSavedGames()
    }

    fun loadSavedGames() {
        viewModelScope.launch {
            _savedGamesState.value = UiState.Loading

            retrieverUseCase(onlyInProgress = true)
                .map { gameList -> gameList.sortedByDescending { it.timestamp }.take(5) }
                .catch { exception ->
                    _savedGamesState.value = UiState.Error(exception.message ?: "Unknown error")
                }
                .collect { sortedGames ->
                    _savedGamesState.value = UiState.Success(sortedGames)
                }
        }
    }
}