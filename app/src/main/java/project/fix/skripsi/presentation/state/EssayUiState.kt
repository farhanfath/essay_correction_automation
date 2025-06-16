package project.fix.skripsi.presentation.state

import project.fix.skripsi.presentation.utils.common.base.state.EssayState
import project.fix.skripsi.presentation.utils.common.base.state.UiState

data class EssayUiState(
  val essayData: EssayData = EssayData(),
  val resultState: EssayState = UiState.Idle
)
