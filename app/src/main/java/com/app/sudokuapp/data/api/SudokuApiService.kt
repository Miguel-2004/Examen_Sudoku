package com.app.sudokuapp.data.api

import com.app.sudokuapp.data.model.SudokuApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SudokuApiService {

    @GET("v1/sudokugenerate")
    suspend fun fetchGeneratedSudoku(
        @Header("X-Api-Key") apiKey: String,
        @Query("difficulty") level: String,
        @Query("width") gridWidth: Int,
        @Query("height") gridHeight: Int
    ): SudokuApiResponse
}