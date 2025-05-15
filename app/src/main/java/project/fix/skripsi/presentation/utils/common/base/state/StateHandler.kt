package project.fix.skripsi.presentation.utils.common.base.state

import androidx.compose.runtime.Composable

@Composable
fun <T> StateHandler(
  state: UiState<T>,
  onIdle: @Composable (() -> Unit)? = null,
  onLoading: @Composable () -> Unit,
  onError: @Composable (String) -> Unit,
  onSuccess: @Composable (T) -> Unit
) {
  when (state) {
    is UiState.Idle -> onIdle?.invoke()
    is UiState.Loading -> onLoading()
    is UiState.Error -> onError(state.message)
    is UiState.Success -> onSuccess(state.data)
  }
}
