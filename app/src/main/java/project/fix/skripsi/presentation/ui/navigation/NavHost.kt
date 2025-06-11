package project.fix.skripsi.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.fix.skripsi.presentation.ui.screen.home.HomeScreen
import project.fix.skripsi.presentation.ui.screen.result.EnhancedResultScreen
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import project.fix.skripsi.presentation.viewmodel.SavedAnswerKeyViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    essayViewModel: EssayViewModel,
    savedAnswerKeyViewModel: SavedAnswerKeyViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToResult = {
                    navController.navigate(Screen.Result)
                },
                essayViewModel = essayViewModel,
                savedAnswerKeyViewModel = savedAnswerKeyViewModel
            )
        }
        composable<Screen.Result> {
            EnhancedResultScreen(
                viewModel = essayViewModel,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}