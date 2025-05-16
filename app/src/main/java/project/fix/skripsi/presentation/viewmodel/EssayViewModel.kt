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

  // Reorder images
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

  // Clear all images
  fun clearSelectedImages() {
    _selectedImageUris.value = emptyList()
  }

  private val _result = MutableStateFlow<EssayState>(UiState.Idle)
  val result: StateFlow<EssayState> = _result.asStateFlow()

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

  fun resetState() {
    clearSelectedImages()
    _result.value = UiState.Idle
  }
}

//@HiltViewModel
//class EssayViewModel @Inject constructor(
//  private val evaluateEssayUseCase: EvaluateEssayUseCase
//) : ViewModel() {
//
//  var selectedImageUris = mutableStateListOf<Uri>()
//    private set
//
//  fun setSelectedImage(uri: Uri) {
//    selectedImageUris.clear()
//    selectedImageUris.add(uri)
//  }
//
//  fun addSelectedImages(uris: List<Uri>) {
//    selectedImageUris.addAll(uris)
//  }
//
//  private fun clearSelectedImages() {
//    selectedImageUris.clear()
//  }
//
//  private val _result = MutableStateFlow<EssayState>(UiState.Idle)
//  val result: StateFlow<EssayState> = _result.asStateFlow()
//
//  fun evaluateEssay(context: Context) {
//    viewModelScope.launch {
//      _result.value = UiState.Loading
//
//      val bitmaps = selectedImageUris.map { uri -> uriToBitmap(context, uri) }
//      if (bitmaps.isEmpty()) {
//        _result.value = UiState.Error("Tidak ada gambar valid")
//        return@launch
//      }
//
//      val mergedBitmap = mergeImagesVertically(bitmaps)
//      val tempFile = bitmapToTempFile(context, mergedBitmap)
//
//      val response = evaluateEssayUseCase(tempFile)
//      _result.value = response.toUiState()
//    }
//  }
//
//  fun resetState() {
//    clearSelectedImages()
//    _result.value = UiState.Idle
//  }
//}