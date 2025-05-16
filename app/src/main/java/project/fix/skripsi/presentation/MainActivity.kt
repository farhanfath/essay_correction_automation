package project.fix.skripsi.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import project.fix.skripsi.presentation.ui.screen.home.EnhancedHomeScreen
import project.fix.skripsi.presentation.ui.screen.result.EnhancedResultScreen
import project.fix.skripsi.presentation.ui.screen.home.HomeScreen
import project.fix.skripsi.presentation.ui.screen.home.HomeScreenV2
import project.fix.skripsi.presentation.ui.screen.home.components.UpgradeHomeScreen
import project.fix.skripsi.presentation.ui.theme.SkripsiappTheme
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Aplikasi membutuhkan izin kamera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestCameraPermission()
        setContent {
            SkripsiappTheme {
                Scaffold { innerPadding ->
                    val navController = rememberNavController()
                    val mainViewModel: EssayViewModel = viewModel()

                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "Home"
                    ) {
                        composable("Home") {
                            UpgradeHomeScreen()
//                            EnhancedHomeScreen(
//                                navController = navController,
//                                viewModel = mainViewModel
//                            )
//                            HomeScreenV2(
//                                navController = navController,
//                                viewModel = mainViewModel
//                            )
//                            HomeScreen(
//                                navController = navController,
//                                viewModel = mainViewModel
//                            )
                        }
                        composable("Result") {
                            EnhancedResultScreen(
                                navController = navController,
                                viewModel = mainViewModel
                            )
                        }
                    }
                }
            }
        }
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    this,
                    "Aplikasi membutuhkan izin kamera untuk mengambil foto",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }
}
