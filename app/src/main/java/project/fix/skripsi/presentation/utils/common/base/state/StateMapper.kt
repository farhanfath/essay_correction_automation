package project.fix.skripsi.presentation.utils.common.base.state

import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.utils.ResultResponse
import project.fix.skripsi.presentation.utils.common.base.state.UiState.Error
import project.fix.skripsi.presentation.utils.common.base.state.UiState.Loading
import project.fix.skripsi.presentation.utils.common.base.state.UiState.Success

fun <T> ResultResponse<T>.toUiState(): UiState<T> = when (this) {
  is ResultResponse.Loading -> Loading
  is ResultResponse.Success -> Success(data)
  is ResultResponse.Error -> Error(message)
}

/**
 * state typealias
 */
typealias EssayState = UiState<HasilKoreksi>