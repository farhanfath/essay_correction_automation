package project.fix.skripsi.presentation.utils.common.base.state

sealed class UiState<out T> {
  data object Idle : UiState<Nothing>()
  data object Loading : UiState<Nothing>()
  data class Success<out T>(val data: T) : UiState<T>()
  data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>()
}
