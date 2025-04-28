package com.app.sudokuapp.presentation.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.usecase.GetSavedGamesUseCase
import com.app.sudokuapp.domain.usecase.SaveGameUseCase
import com.app.sudokuapp.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedGamesViewModel @Inject constructor(
    private val retrieverUseCase: GetSavedGamesUseCase,
    private val saverUseCase: SaveGameUseCase
) : ViewModel() {

    private val _savedGamesState = MutableStateFlow<UiState<List<Sudoku>>>(UiState.Loading)
    val savedGamesState: StateFlow<UiState<List<Sudoku>>> = _savedGamesState.asStateFlow()

    private val _currentFilter = MutableStateFlow(FilterType.ALL)
    val currentFilter: StateFlow<FilterType> = _currentFilter.asStateFlow()

    init {
        loadSavedGames()
    }

    fun loadSavedGames() {
        viewModelScope.launch {
            _savedGamesState.value = UiState.Loading

            retrieverUseCase(onlyInProgress = _currentFilter.value == FilterType.IN_PROGRESS)
                .catch { exception ->
                    _savedGamesState.value = UiState.Error(exception.message ?: "Unknown error")
                }
                .collect { gameList ->
                    val filtered = when (_currentFilter.value) {
                        FilterType.ALL -> gameList
                        FilterType.IN_PROGRESS -> gameList.filter { !it.isCompleted }
                        FilterType.COMPLETED -> gameList.filter { it.isCompleted }
                    }
                    _savedGamesState.value = UiState.Success(filtered)
                }
        }
    }

    fun updateFilter(filterType: FilterType) {
        _currentFilter.value = filterType
        loadSavedGames()
    }

    fun deleteGame(gameId: String) {
        viewModelScope.launch {
            try {
                saverUseCase.removeSavedGame(gameId)
                loadSavedGames()
            } catch (_: Exception) {
            }
        }
    }
}

enum class FilterType {
    ALL,
    IN_PROGRESS,
    COMPLETED
}