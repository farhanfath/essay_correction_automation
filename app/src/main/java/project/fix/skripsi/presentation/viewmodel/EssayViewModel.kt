package project.fix.skripsi.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.CorrectionType
import project.fix.skripsi.domain.model.EssayCategory
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
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

  private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
  val selectedImageUris = _selectedImageUris.asStateFlow()

  private val _result = MutableStateFlow<EssayState>(UiState.Idle)
  val result: StateFlow<EssayState> = _result.asStateFlow()

  private val _selectedCategory = MutableStateFlow<EssayCategory?>(null)
  val selectedCategory = _selectedCategory.asStateFlow()

  private val _answerKeyItems = MutableStateFlow<List<AnswerKeyItem>>(emptyList())
  val answerKeyItems = _answerKeyItems.asStateFlow()

  private val _correctionType = MutableStateFlow(CorrectionType.AI)
  val correctionType = _correctionType.asStateFlow()

  fun evaluateEssay(context: Context) {
    viewModelScope.launch {
      _result.value = UiState.Loading

      val imagesList = _selectedImageUris.value
      if (imagesList.isEmpty()) {
        _result.value = UiState.Error("Tidak ada gambar valid")
        return@launch
      }

      // image for send to evaluation
      val bitmaps = imagesList.map { uri -> uriToBitmap(context, uri) }
      val mergedBitmap = mergeImagesVertically(bitmaps)
      val tempFile = bitmapToTempFile(context, mergedBitmap)

      // category
      val category = _selectedCategory.value
      val quizCategoryType = category?.id ?: ""

      // answer_key
      val answerKeysList = answerKeyItems.value.map { it.answer }
      val correctionType = correctionType.value.name

      val response = evaluateEssayUseCase(tempFile, quizCategoryType, correctionType, answerKeysList)
      _result.value = response.toUiState()
    }
  }

  fun addSingleImage(uri: Uri) {
    val currentList = _selectedImageUris.value.toMutableList()
    currentList.add(uri)
    _selectedImageUris.value = currentList
  }

  // Add multiple images
  fun addSelectedImages(uris: List<Uri>) {
    val currentList = _selectedImageUris.value.toMutableList()
    currentList.addAll(uris)
    _selectedImageUris.value = currentList
  }

  // Remove an image at a specific index
  fun removeImage(index: Int) {
    if (index in _selectedImageUris.value.indices) {
      val currentList = _selectedImageUris.value.toMutableList()
      currentList.removeAt(index)
      _selectedImageUris.value = currentList
    }
  }

  // Reorder images - Metode dengan fromIndex dan toIndex
  fun reorderImages(fromIndex: Int, toIndex: Int) {
    if (fromIndex in _selectedImageUris.value.indices &&
      toIndex in _selectedImageUris.value.indices &&
      fromIndex != toIndex) {

      val currentList = _selectedImageUris.value.toMutableList()
      val item = currentList.removeAt(fromIndex)
      currentList.add(toIndex, item)
      _selectedImageUris.value = currentList
    }
  }

  // Metode baru untuk memperbarui seluruh urutan gambar
  fun updateImagesOrder(newOrderedUris: List<Uri>) {
    _selectedImageUris.value = newOrderedUris
  }

  // Clear all images
  fun clearSelectedImages() {
    _selectedImageUris.value = emptyList()
  }

  fun resetState() {
    clearSelectedImages()
    _result.value = UiState.Idle
  }

  // category
  fun setSelectedCategory(category: EssayCategory) {
    _selectedCategory.value = category
  }

  // answer key handler
  fun updateAnswerKeyItems(items: List<AnswerKeyItem>) {
    _answerKeyItems.value = items
  }

  fun setCorrectionType(type: CorrectionType) {
    _correctionType.value = type
  }
}