package project.fix.skripsi.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import project.fix.skripsi.presentation.ui.screen.history.ScoreHistoryScreen
import project.fix.skripsi.presentation.ui.screen.home.HomeScreen
import project.fix.skripsi.presentation.ui.screen.result.ResultScreen
import project.fix.skripsi.presentation.ui.screen.result.state.ResultScreenState
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import project.fix.skripsi.presentation.viewmodel.SavedAnswerKeyViewModel
import project.fix.skripsi.presentation.viewmodel.SavedScoreHistoryViewModel

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val essayViewModel: EssayViewModel = hiltViewModel()
    val savedAnswerKeyViewModel: SavedAnswerKeyViewModel = hiltViewModel()
    val savedScoreHistoryViewModel: SavedScoreHistoryViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToResult = {
                    navController.navigate(Screen.Result)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History)
                },
                essayViewModel = essayViewModel,
                savedAnswerKeyViewModel = savedAnswerKeyViewModel
            )
        }
        composable<Screen.Result> {
            ResultScreen(
                essayViewModel = essayViewModel,
                savedScoreHistoryViewModel = savedScoreHistoryViewModel,
                mode = ResultScreenState.FRESH_RESULT,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.History> {
            ScoreHistoryScreen(
                scoreHistoryViewModel = savedScoreHistoryViewModel,
                onNavigateToDetailHistory = { historyId ->
                    navController.navigate(Screen.DetailHistory(historyId))
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.DetailHistory> { backStackEntry ->
            val detailHistory = backStackEntry.toRoute<Screen.DetailHistory>()
            ResultScreen(
                essayViewModel = null,
                savedScoreHistoryViewModel = savedScoreHistoryViewModel,
                mode = ResultScreenState.HISTORY_DETAIL,
                historyId = detailHistory.historyId,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}