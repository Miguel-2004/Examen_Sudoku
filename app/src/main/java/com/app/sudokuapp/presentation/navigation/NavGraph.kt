package com.app.sudokuapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.sudokuapp.domain.model.SudokuDifficulty
import com.app.sudokuapp.domain.model.SudokuSize
import com.app.sudokuapp.presentation.screens.game.GameScreen
import com.app.sudokuapp.presentation.screens.game.GameViewModel
import com.app.sudokuapp.presentation.screens.home.HomeScreen
import com.app.sudokuapp.presentation.screens.home.HomeViewModel

sealed class AppScreen(val route: String) {
    data object Home : AppScreen("home")
    data object NewGame : AppScreen("game/{size}/{difficulty}") {
        fun createNewGameRoute(size: SudokuSize, difficulty: SudokuDifficulty): String {
            return "game/${size.name}/${difficulty.name}"
        }
    }
    data object ResumeGame : AppScreen("game/continue/{gameId}") {
        fun createResumeGameRoute(gameId: String): String {
            return "game/continue/$gameId"
        }
    }
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppScreen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppScreen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onStartNewGame = { size, difficulty ->
                    navController.navigate(AppScreen.NewGame.createNewGameRoute(size, difficulty))
                },
                onContinueGame = { gameId ->
                    navController.navigate(AppScreen.ResumeGame.createResumeGameRoute(gameId))
                }
            )
        }

        composable(
            route = AppScreen.NewGame.route,
            arguments = listOf(
                navArgument("size") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) {
            val gameViewModel: GameViewModel = hiltViewModel()
            GameScreen(
                viewModel = gameViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = AppScreen.ResumeGame.route,
            arguments = listOf(
                navArgument("gameId") { type = NavType.StringType }
            )
        ) {
            val gameViewModel: GameViewModel = hiltViewModel()
            GameScreen(
                viewModel = gameViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}