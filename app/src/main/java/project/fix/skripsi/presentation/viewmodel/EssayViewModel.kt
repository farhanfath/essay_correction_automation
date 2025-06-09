package project.fix.skripsi.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.CorrectionType
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
import project.fix.skripsi.presentation.state.EssayData
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

  private val _result = MutableStateFlow<EssayState>(UiState.Idle)
  val result: StateFlow<EssayState> = _result.asStateFlow()

  private val _essayData = MutableStateFlow(EssayData())
  val essayData = _essayData.asStateFlow()

  fun evaluateEssay(context: Context) {
    viewModelScope.launch {
      _result.value = UiState.Loading

      val imagesList = essayData.value.selectedImageUris
      if (imagesList.isEmpty()) {
        _result.value = UiState.Error("Tidak ada gambar valid")
        return@launch
      }

      val bitmaps = imagesList.map { uri -> uriToBitmap(context, uri) }
      val mergedBitmap = mergeImagesVertically(bitmaps)
      val tempFile = bitmapToTempFile(context, mergedBitmap)

      val answerKeysList = essayData.value.answerKeyItems
      val correctionType = essayData.value.correctionType.name

      val response = evaluateEssayUseCase(tempFile, correctionType, answerKeysList)
      _result.value = response.toUiState()
    }
  }

  fun addSingleImage(uri: Uri) {
    _essayData.update { it.copy(selectedImageUris = it.selectedImageUris + uri) }
  }

  fun addSelectedImages(uris: List<Uri>) {
    _essayData.update { it.copy(selectedImageUris = it.selectedImageUris + uris) }
  }

  fun removeImage(index: Int) {
    _essayData.update {
      val currentList = it.selectedImageUris.toMutableList()
      if (index in currentList.indices) {
        currentList.removeAt(index)
      }
      it.copy(selectedImageUris = currentList)
    }
  }

  fun reorderImages(fromIndex: Int, toIndex: Int) {
    _essayData.update {
      val list = it.selectedImageUris.toMutableList()
      if (fromIndex in list.indices && toIndex in list.indices && fromIndex != toIndex) {
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
      }
      it.copy(selectedImageUris = list)
    }
  }

  fun updateImagesOrder(newOrderedUris: List<Uri>) {
    _essayData.update { it.copy(selectedImageUris = newOrderedUris) }
  }

  fun clearSelectedImages() {
    _essayData.update { it.copy(selectedImageUris = emptyList()) }
  }

  fun resetState() {
    _essayData.value = EssayData() // Reset all to default
    _result.value = UiState.Idle
  }

  fun updateAnswerKeyItems(items: List<AnswerKeyItem>) {
    _essayData.update { it.copy(answerKeyItems = items) }
  }

  fun setCorrectionType(type: CorrectionType) {
    _essayData.update { it.copy(correctionType = type) }
  }
}