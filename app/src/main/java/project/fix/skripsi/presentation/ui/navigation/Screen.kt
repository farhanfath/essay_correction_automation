package project.fix.skripsi.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {

    @Serializable
    data object Home: Screen(route = "Home")

    @Serializable
    data object Result: Screen(route = "Result")

    @Serializable
    data object History: Screen(route = "History")

    @Serializable
    data class DetailHistory(
        val id: Long
    ): Screen(route = "Detail_History")
}