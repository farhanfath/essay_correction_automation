package project.fix.skripsi.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Scaffold { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
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
                    navController = navController,
                    viewModel = essayViewModel
                )
            }
        }
    }
}