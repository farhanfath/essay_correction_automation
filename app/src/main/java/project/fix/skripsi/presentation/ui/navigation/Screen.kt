package project.fix.skripsi.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {

    @Serializable
    data object Home: Screen(route = "Home")

    @Serializable
    data object Result: Screen(route = "Result")
}