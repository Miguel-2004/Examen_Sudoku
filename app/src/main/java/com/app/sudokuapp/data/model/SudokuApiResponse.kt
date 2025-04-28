package com.app.sudokuapp.data.model

import com.google.gson.annotations.SerializedName

data class SudokuApiResponse(
    @SerializedName("puzzle") val puzzleGrid: List<List<Int?>>,
    @SerializedName("solution") val solutionGrid: List<List<Int>>
)