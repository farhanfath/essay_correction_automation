package project.fix.skripsi.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
import project.fix.skripsi.presentation.utils.common.base.state.EssayState
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.common.base.state.toUiState
import project.fix.skripsi.presentation.utils.helper.bitmapToTempFile
import project.fix.skripsi.presentation.utils.helper.mergeImagesVertically
import project.fix.skripsi.presentation.utils.helper.uriToBitmap
import project.fix.skripsi.presentation.utils.helper.uriToTempFile
import javax.inject.Inject


@HiltViewModel
class EssayViewModel @Inject constructor(
  private val evaluateEssayUseCase: EvaluateEssayUseCase
) : ViewModel() {

  private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
  val selectedImageUris = _selectedImageUris.asStateFlow()

  private val _result = MutableStateFlow<EssayState>(UiState.Idle)
  val result: StateFlow<EssayState> = _result.asStateFlow()

  // Show/hide preview row state
  private val _showPreviewRow = MutableStateFlow(true)
  val showPreviewRow = _showPreviewRow.asStateFlow()

  // Show/hide media options state
  private val _showMediaOptions = MutableStateFlow(false)
  val showMediaOptions = _showMediaOptions.asStateFlow()

  fun evaluateEssay(context: Context) {
    viewModelScope.launch {
      _result.value = UiState.Loading

      val imagesList = _selectedImageUris.value
      if (imagesList.isEmpty()) {
        _result.value = UiState.Error("Tidak ada gambar valid")
        return@launch
      }

      val bitmaps = imagesList.map { uri -> uriToBitmap(context, uri) }
      val mergedBitmap = mergeImagesVertically(bitmaps)
      val tempFile = bitmapToTempFile(context, mergedBitmap)

      val response = evaluateEssayUseCase(tempFile)
      _result.value = response.toUiState()
    }
  }

  fun addSingleImage(uri: Uri) {
    val currentList = _selectedImageUris.value.toMutableList()
    currentList.add(uri)
    _selectedImageUris.value = currentList
  }

  /**
   * todo: delete soon
   */
  // Set a single image (replacing all existing ones)
  fun setSelectedImage(uri: Uri) {
    _selectedImageUris.value = listOf(uri)
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

  // Toggle preview row visibility
  fun togglePreviewRow(show: Boolean) {
    _showPreviewRow.value = show
  }

  // Toggle media options visibility
  fun toggleMediaOptions(show: Boolean) {
    _showMediaOptions.value = show
  }

  // Clear all images
  fun clearSelectedImages() {
    _selectedImageUris.value = emptyList()
  }

  fun resetState() {
    clearSelectedImages()
    _result.value = UiState.Idle
  }
}