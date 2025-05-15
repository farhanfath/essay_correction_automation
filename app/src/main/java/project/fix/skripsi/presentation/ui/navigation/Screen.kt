package project.fix.skripsi.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {

  @Serializable
   data object Main: Screen(route = "main")
}