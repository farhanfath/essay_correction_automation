package project.fix.skripsi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.usecase.SavedScoreHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class SavedScoreHistoryViewModel @Inject constructor(
  private val savedScoreHistoryUseCase: SavedScoreHistoryUseCase
) : ViewModel() {
  private val _savedScoreHistoryList = MutableStateFlow<List<SavedScoreHistory>>(emptyList())
  val savedScoreHistoryList = _savedScoreHistoryList.asStateFlow()

  private val _savedScoreHistory = MutableStateFlow<SavedScoreHistory?>(null)
  val savedScoreHistory = _savedScoreHistory.asStateFlow()

  init {
    getAllSavedScoreHistory()
  }

  private fun getAllSavedScoreHistory() {
    viewModelScope.launch {
      val savedScoreData = savedScoreHistoryUseCase.getAllSavedScoreHistory()
      _savedScoreHistoryList.value = savedScoreData
    }
  }

  fun addSavedScoreHistory(title: String, hasilKoreksi: List<HasilKoreksi>) {
    val savedData = SavedScoreHistory(
      id = 0,
      title = title,
      hasilKoreksi = hasilKoreksi,
      createdAt = System.currentTimeMillis()
    )
    viewModelScope.launch {
      savedScoreHistoryUseCase.insertSavedScoreHistory(savedData)
    }
  }

  fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistory) {
    viewModelScope.launch {
      savedScoreHistoryUseCase.updateSavedScoreHistory(savedScoreHistory)
    }
  }

  fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistory) {
    viewModelScope.launch {
      savedScoreHistoryUseCase.deleteSavedScoreHistory(savedScoreHistory)
    }
  }

  fun deleteSavedScoreHistoryById(id: Long) {
    viewModelScope.launch {
      savedScoreHistoryUseCase.deleteSavedScoreHistoryById(id)
    }
  }

  fun getSavedScoreHistoryById(id: Long) {
    viewModelScope.launch {
      val data = savedScoreHistoryUseCase.getSavedScoreHistoryById(id)
      _savedScoreHistory.value = data
    }
  }
}