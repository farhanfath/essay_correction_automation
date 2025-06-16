package project.fix.skripsi.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.constants.CorrectionType
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
import project.fix.skripsi.presentation.state.EssayData
import project.fix.skripsi.presentation.state.EssayUiState
import project.fix.skripsi.presentation.utils.common.base.state.EssayState
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.common.base.state.toUiState
import project.fix.skripsi.presentation.utils.helper.bitmapToTempFile
import project.fix.skripsi.presentation.utils.helper.mergeImagesVertically
import project.fix.skripsi.presentation.utils.helper.uriToBitmap
import javax.inject.Inject


@HiltViewModel
class EssayViewModel @Inject constructor(
  private val evaluateEssayUseCase: EvaluateEssayUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow(EssayUiState())
  val uiState = _uiState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    EssayUiState()
  )

  // helper function
  private fun updateEssayData(update: EssayData.() -> EssayData) {
    _uiState.update { current ->
      current.copy(essayData = current.essayData.update())
    }
  }

  fun evaluateEssay(context: Context) {
    viewModelScope.launch {
      val essayData = _uiState.value.essayData
      val imagesList = essayData.selectedImageUris

      if (imagesList.isEmpty()) {
        _uiState.update { it.copy(resultState = UiState.Error("Tidak ada gambar valid")) }
        return@launch
      }

      val bitmaps = imagesList.map { uri -> uriToBitmap(context, uri) }
      val mergedBitmap = mergeImagesVertically(bitmaps)
      val tempFile = bitmapToTempFile(context, mergedBitmap)

      val answerKeysList = essayData.answerKeyItems
      val correctionType = essayData.correctionType.name

      val response = evaluateEssayUseCase(tempFile, correctionType, answerKeysList)
      _uiState.update { it.copy(resultState = response.toUiState()) }
    }
  }

  fun addSingleImage(uri: Uri) {
    updateEssayData {
      copy(selectedImageUris = selectedImageUris + uri)
    }
  }

  fun addSelectedImages(uris: List<Uri>) {
    updateEssayData {
      copy(selectedImageUris = selectedImageUris + uris)
    }
  }

  fun removeImage(index: Int) {
    updateEssayData {
      val currentList = selectedImageUris.toMutableList()
      if (index in currentList.indices) {
        currentList.removeAt(index)
      }
      copy(selectedImageUris = currentList)
    }
  }

  fun reorderImages(fromIndex: Int, toIndex: Int) {
    updateEssayData {
      val list = selectedImageUris.toMutableList()
      if (fromIndex in list.indices && toIndex in list.indices && fromIndex != toIndex) {
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
      }
      copy(selectedImageUris = list)
    }
  }

  fun updateImagesOrder(newOrderedUris: List<Uri>) {
    updateEssayData {
      copy(selectedImageUris = newOrderedUris)
    }
  }

  fun clearSelectedImages() {
    updateEssayData {
      copy(selectedImageUris = emptyList())
    }
  }

  fun resetState() {
    _uiState.update { current ->
      current.copy(
        essayData = EssayData(), // Reset all to default
        resultState = UiState.Idle
      )
    }
  }

  fun updateAnswerKeyItems(items: List<AnswerKeyItem>) {
    updateEssayData {
      copy(answerKeyItems = items)
    }
  }

  fun setCorrectionType(type: CorrectionType) {
    updateEssayData {
      copy(correctionType = type)
    }
  }
}