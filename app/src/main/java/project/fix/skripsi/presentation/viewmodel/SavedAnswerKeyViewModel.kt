package project.fix.skripsi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.SavedAnswerKey
import project.fix.skripsi.domain.usecase.SavedAnswerKeyUseCase
import javax.inject.Inject

@HiltViewModel
class SavedAnswerKeyViewModel @Inject constructor(
    private val savedAnswerKeyUseCase: SavedAnswerKeyUseCase
) : ViewModel() {
    private val _savedAnswerKeys = MutableStateFlow<List<SavedAnswerKey>>(emptyList())
    val savedAnswerKeys= _savedAnswerKeys.asStateFlow()

    init {
        getAllSavedAnswerKeys()
    }

    fun insertSavedAnswerKey(title: String, answerKeys: List<AnswerKeyItem>) {
        val answerKey = SavedAnswerKey(
            id = 0,
            title = title,
            createdAt = System.currentTimeMillis(),
            answerKeys = answerKeys
        )
        viewModelScope.launch {
            savedAnswerKeyUseCase.insertAnswerKey(answerKey)
            getAllSavedAnswerKeys()
        }
    }

    private fun getAllSavedAnswerKeys() {
        viewModelScope.launch {
            val answerKeys = savedAnswerKeyUseCase.getAllAnswerKeys()
            _savedAnswerKeys.value = answerKeys
        }
    }

    fun getAnswerKeyById(id: Int) {
        viewModelScope.launch {
            savedAnswerKeyUseCase.getAnswerKeyById(id)
        }
    }

    fun updateSavedAnswerKey(answerKey: SavedAnswerKey) {
        viewModelScope.launch {
            savedAnswerKeyUseCase.updateAnswerKey(answerKey)
            getAllSavedAnswerKeys()
        }
    }

    fun deleteSavedAnswerKeyById(id: Int) {
        viewModelScope.launch {
            savedAnswerKeyUseCase.deleteAnswerKeyById(id)
            getAllSavedAnswerKeys()
        }
    }
}