package project.fix.skripsi.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
import project.fix.skripsi.presentation.utils.common.base.state.EssayState
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.common.base.state.toUiState
import project.fix.skripsi.presentation.utils.helper.uriToTempFile
import javax.inject.Inject

@HiltViewModel
class EssayViewModel @Inject constructor(
  private val evaluateEssayUseCase: EvaluateEssayUseCase
) : ViewModel() {

  var selectedImageUri by mutableStateOf<Uri?>(null)
      private set

  private val _result = MutableStateFlow<EssayState>(UiState.Idle)
  val result: StateFlow<EssayState> = _result.asStateFlow()

  fun evaluateEssay(context: Context, uri: Uri) {
    viewModelScope.launch {
      _result.value = UiState.Loading

      val imageFile = uriToTempFile(context, uri)
      val response = evaluateEssayUseCase(imageFile)

      _result.value = response.toUiState()
    }
  }

  fun setSelectedImage(uri: Uri?) {
      selectedImageUri = uri
  }

  fun resetState() {
    selectedImageUri = null
    _result.value = UiState.Idle
  }
}