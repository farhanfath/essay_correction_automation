package project.fix.skripsi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import project.fix.skripsi.presentation.ui.navigation.AppNavHost
import project.fix.skripsi.presentation.ui.theme.SkripsiappTheme
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: EssayViewModel = hiltViewModel()
            val navController = rememberNavController()

            EssayApp {
                AppNavHost(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun EssayApp(content: @Composable () -> Unit) {
    SkripsiappTheme(
        dynamicColor = false
    ) {
        content()
    }
}
