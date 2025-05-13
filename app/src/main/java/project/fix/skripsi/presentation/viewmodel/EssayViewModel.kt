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
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
import project.fix.skripsi.domain.utils.ResultResponse
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EssayViewModel @Inject constructor(
  private val evaluateEssayUseCase: EvaluateEssayUseCase
) : ViewModel() {

  var selectedImageUri by mutableStateOf<Uri?>(null)
      private set

  private val _result = MutableStateFlow<ResultResponse<HasilKoreksi>>(ResultResponse.Initiate)
  val result: StateFlow<ResultResponse<HasilKoreksi>> = _result.asStateFlow()

  fun evaluateEssay(context: Context, uri: Uri) {
    viewModelScope.launch {
      _result.value = ResultResponse.Loading
      try {
        val imageFile = uriToTempFile(context, uri)
        val response = evaluateEssayUseCase(imageFile)
        _result.value = response
      } catch (e: Exception) {
        _result.value = ResultResponse.Error(e.message?: "")
      }
    }
  }

  fun setSelectedImage(uri: Uri?) {
      selectedImageUri = uri
  }

  private fun uriToTempFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("essay_image", ".jpg", context.cacheDir)
    inputStream?.use { input ->
      FileOutputStream(tempFile).use { output ->
        input.copyTo(output)
      }
    }
    return tempFile
  }

  fun resetState() {
    selectedImageUri = null
    _result.value = ResultResponse.Initiate
  }
}

//class MainViewModel @Inject constructor(
//    private val repository: N8nRepository
//) : ViewModel() {
//
//    var selectedImageUri by mutableStateOf<Uri?>(null)
//        private set
//
//    var isLoading by mutableStateOf(false)
//        private set
//
//    var evaluationResults by mutableStateOf<List<N8nResponse>?>(null)
//        private set
//
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    fun setSelectedImage(uri: Uri?) {
//        selectedImageUri = uri
//    }
//
//    fun evaluateEssay(context: Context) {
//        val imageUri = selectedImageUri ?: return
//
//        isLoading = true
//        errorMessage = null
//
//        viewModelScope.launch {
//            repository.evaluateEssay(imageUri, context)
//                .onSuccess { results ->
//                    evaluationResults = results
//                    isLoading = false
//                }
//                .onFailure { error ->
//                    errorMessage = error.message ?: "Terjadi kesalahan"
//                    isLoading = false
//                }
//        }
//    }
//
//    fun resetState() {
//        selectedImageUri = null
//        evaluationResults = null
//        errorMessage = null
//    }
//}