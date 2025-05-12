package project.fix.skripsi.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import project.fix.skripsi.data.remote.n8n.model.N8nResponse
import project.fix.skripsi.domain.repository.N8nRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: N8nRepository
) : ViewModel() {

    var selectedImageUri by mutableStateOf<Uri?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var evaluationResults by mutableStateOf<List<N8nResponse>?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun setSelectedImage(uri: Uri?) {
        selectedImageUri = uri
    }

    fun evaluateEssay(context: Context) {
        val imageUri = selectedImageUri ?: return

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            repository.evaluateEssay(imageUri, context)
                .onSuccess { results ->
                    evaluationResults = results
                    isLoading = false
                }
                .onFailure { error ->
                    errorMessage = error.message ?: "Terjadi kesalahan"
                    isLoading = false
                }
        }
    }

    fun resetState() {
        selectedImageUri = null
        evaluationResults = null
        errorMessage = null
    }
}