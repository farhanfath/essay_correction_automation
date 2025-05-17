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

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: EssayViewModel
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
                    viewModel = viewModel
                )
            }
            composable<Screen.Result> {
                EnhancedResultScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}