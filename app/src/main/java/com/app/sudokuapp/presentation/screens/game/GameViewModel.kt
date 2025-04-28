package com.app.sudokuapp.presentation.screens.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sudokuapp.domain.model.Sudoku
import com.app.sudokuapp.domain.model.SudokuCell
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import com.app.sudokuapp.domain.usecase.GenerateSudokuUseCase
import com.app.sudokuapp.domain.usecase.GetSavedGamesUseCase
import com.app.sudokuapp.domain.usecase.SaveGameUseCase
import com.app.sudokuapp.domain.usecase.VerifySolutionUseCase
import com.app.sudokuapp.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.get
import kotlin.text.get

@HiltViewModel
class GameViewModel @Inject constructor(
    private val generatorUseCase: GenerateSudokuUseCase,
    private val retrieverUseCase: GetSavedGamesUseCase,
    private val saverUseCase: SaveGameUseCase,
    private val verifierUseCase: VerifySolutionUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _sudokuState = MutableStateFlow<UiState<Sudoku>>(UiState.Loading)
    val sudokuState: StateFlow<UiState<Sudoku>> = _sudokuState.asStateFlow()

    private val _selectedCell = MutableStateFlow<SudokuCell?>(null)
    val selectedCell: StateFlow<SudokuCell?> = _selectedCell.asStateFlow()

    private val _isSolved = MutableStateFlow(false)
    val isSolved: StateFlow<Boolean> = _isSolved.asStateFlow()

    private val _isGameSaved = MutableStateFlow(false)
    val isGameSaved: StateFlow<Boolean> = _isGameSaved.asStateFlow()

    private val _isNoteMode = MutableStateFlow(false)
    val isNoteMode: StateFlow<Boolean> = _isNoteMode.asStateFlow()

    init {
        val gameId = savedStateHandle.get<String>("gameId")
        if (gameId != null) {
            loadExistingGame(gameId)
        } else {
            val sizeParam = savedStateHandle.get<String>("size") ?: SudokuSize.STANDARD.name
            val difficultyParam = savedStateHandle.get<String>("difficulty") ?: SudokuDifficulty.EASY.name

            val size = SudokuSize.valueOf(sizeParam)
            val difficulty = SudokuDifficulty.valueOf(difficultyParam)

            createNewGame(size, difficulty)
        }
    }

    private fun loadExistingGame(gameId: String) {
        viewModelScope.launch {
            _sudokuState.value = UiState.Loading
            try {
                val sudoku = retrieverUseCase.retrieveSudokuById(gameId)
                if (sudoku != null) {
                    _sudokuState.value = UiState.Success(sudoku)
                    _isGameSaved.value = true
                    checkIfGameSolved(sudoku)
                } else {
                    _sudokuState.value = UiState.Error("Game not found")
                }
            } catch (e: Exception) {
                _sudokuState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createNewGame(size: SudokuSize, difficulty: SudokuDifficulty) {
        viewModelScope.launch {
            _sudokuState.value = UiState.Loading
            _selectedCell.value = null
            _isSolved.value = false
            _isGameSaved.value = false

            try {
                val result = generatorUseCase(size, difficulty)
                result.onSuccess { sudoku ->
                    _sudokuState.value = UiState.Success(sudoku)
                    _isGameSaved.value = true
                }.onFailure { error ->
                    _sudokuState.value = UiState.Error(error.message ?: "Failed to generate Sudoku")
                }
            } catch (e: Exception) {
                _sudokuState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun selectCell(rowIndex: Int, colIndex: Int) {
        val currentState = _sudokuState.value
        if (currentState !is UiState.Success) return

        val sudoku = currentState.result

        if (sudoku.puzzle[rowIndex][colIndex] != null) return

        val selected = SudokuCell(
            rowIndex = rowIndex,
            colIndex = colIndex,
            value = sudoku.currentState[rowIndex][colIndex],
            isOriginal = false,
            isValid = verifierUseCase.validateSingleCell(sudoku, rowIndex, colIndex),
            notes = emptySet()
        )

        _selectedCell.value = selected
    }

    fun setValueForSelectedCell(value: Int?) {
        val cell = _selectedCell.value ?: return
        val currentState = _sudokuState.value

        if (currentState !is UiState.Success) return

        val sudoku = currentState.result
        val newState = sudoku.currentState.map { it.toMutableList() }
        newState[cell.rowIndex][cell.colIndex] = value

        val updatedSudoku = sudoku.copy(currentState = newState)
        _sudokuState.value = UiState.Success(updatedSudoku)

        viewModelScope.launch {
            saverUseCase.updateCurrentGame(sudoku.id, newState, false)
        }

        _selectedCell.value = cell.copy(
            value = value,
            isValid = verifierUseCase.validateSingleCell(updatedSudoku, cell.rowIndex, cell.colIndex)
        )

        checkIfGameSolved(updatedSudoku)
    }

    private fun checkIfGameSolved(sudoku: Sudoku) {
        val solved = verifierUseCase(sudoku)
        _isSolved.value = solved

        if (solved) {
            viewModelScope.launch {
                saverUseCase.updateCurrentGame(sudoku.id, sudoku.currentState, true)
            }
        }
    }

    fun saveGame() {
        val currentState = _sudokuState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            try {
                saverUseCase(currentState.result)
                _isGameSaved.value = true
            } catch (e: Exception) {
            }
        }
    }

    fun resetGame() {
        val currentState = _sudokuState.value
        if (currentState !is UiState.Success) return

        val sudoku = currentState.result
        val resetState = sudoku.puzzle.map { it.toMutableList() }

        val resetSudoku = sudoku.copy(
            currentState = resetState,
            isCompleted = false
        )

        _sudokuState.value = UiState.Success(resetSudoku)
        _selectedCell.value = null
        _isSolved.value = false

        viewModelScope.launch {
            saverUseCase.updateCurrentGame(sudoku.id, resetState, false)
        }
    }

    fun toggleNoteMode() {
        _isNoteMode.value = !_isNoteMode.value
    }

    fun toggleNote(number: Int) {
        val cell = _selectedCell.value ?: return
        val currentState = _sudokuState.value

        if (currentState !is UiState.Success) return

        if (cell.value != null) return

        val updatedNotes = cell.notes.toMutableSet().apply {
            if (contains(number)) remove(number) else add(number)
        }

        _selectedCell.value = cell.copy(notes = updatedNotes)
    }

    fun verifySolution() {
        val currentState = _sudokuState.value
        if (currentState !is UiState.Success) return

        val sudoku = currentState.result
        val solved = verifierUseCase(sudoku)
        _isSolved.value = solved

        if (solved) {
            viewModelScope.launch {
                saverUseCase.updateCurrentGame(sudoku.id, sudoku.currentState, true)
            }
        }
    }
}