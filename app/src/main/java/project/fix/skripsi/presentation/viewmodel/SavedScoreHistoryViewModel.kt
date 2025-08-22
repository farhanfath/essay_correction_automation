package project.fix.skripsi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.model.SiswaData
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

  private val _isLoading = MutableStateFlow(false)
  val isLoading = _isLoading.asStateFlow()

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage = _errorMessage.asStateFlow()

  init {
    getAllSavedScoreHistory()
  }

  private fun getAllSavedScoreHistory() {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        val savedScoreData = savedScoreHistoryUseCase.getAllSavedScoreHistory()
        _savedScoreHistoryList.value = savedScoreData
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal memuat data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
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
      try {
        _isLoading.value = true
        savedScoreHistoryUseCase.insertSavedScoreHistory(savedData)
        getAllSavedScoreHistory()
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal menyimpan data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * Menggabungkan data hasil koreksi baru dengan data yang sudah ada
   * Logic:
   * 1. Jika evaluationType & answerKey sama → merge resultData (gabung siswa)
   * 2. Jika berbeda → tambah sebagai HasilKoreksi baru
   */
  fun mergeWithExistingData(
    existingHistory: SavedScoreHistory,
    newTitle: String,
    newHasilKoreksi: List<HasilKoreksi>
  ) {
    viewModelScope.launch {
      try {
        _isLoading.value = true

        val mergedHasilKoreksi = mutableListOf<HasilKoreksi>()

        // Start with existing data
        mergedHasilKoreksi.addAll(existingHistory.hasilKoreksi)

        // Process each new HasilKoreksi
        newHasilKoreksi.forEach { newHasil ->
          val matchingExistingIndex = mergedHasilKoreksi.indexOfFirst { existing ->
            isSameEvaluationContext(existing, newHasil)
          }

          if (matchingExistingIndex != -1) {
            // Found matching evaluation context, merge the students
            val existingHasil = mergedHasilKoreksi[matchingExistingIndex]
            val mergedStudents = mutableListOf<SiswaData>()

            // Add existing students
            mergedStudents.addAll(existingHasil.resultData)

            // Add new students (check for duplicates)
            newHasil.resultData.forEach { newStudent ->
              val isDuplicate = mergedStudents.any { existing ->
                existing.nama.equals(newStudent.nama, ignoreCase = true)
              }
              if (!isDuplicate) {
                mergedStudents.add(newStudent)
              }
            }

            // Update the existing HasilKoreksi with merged students
            val updatedHasil = existingHasil.copy(resultData = mergedStudents)
            mergedHasilKoreksi[matchingExistingIndex] = updatedHasil
          } else {
            // No matching evaluation context, add as new HasilKoreksi
            mergedHasilKoreksi.add(newHasil)
          }
        }

        // Update data dengan hasil yang sudah digabung
        val updatedHistory = existingHistory.copy(
          title = newTitle,
          hasilKoreksi = mergedHasilKoreksi,
          createdAt = System.currentTimeMillis() // Update timestamp
        )

        savedScoreHistoryUseCase.updateSavedScoreHistory(updatedHistory)
        getAllSavedScoreHistory()
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal menggabungkan data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * Cek apakah dua HasilKoreksi memiliki konteks evaluasi yang sama
   * (evaluationType dan answerKey yang sama)
   */
  private fun isSameEvaluationContext(
    existing: HasilKoreksi,
    new: HasilKoreksi
  ): Boolean {
    // Check if evaluation type is the same
    if (existing.evaluationType != new.evaluationType) {
      return false
    }

    // Check if answer keys are the same (same size and content)
    if (existing.listAnswerKey.size != new.listAnswerKey.size) {
      return false
    }

    // Check each answer key item
    return existing.listAnswerKey.zip(new.listAnswerKey).all { (existingKey, newKey) ->
      existingKey.number == newKey.number &&
              existingKey.answer.equals(newKey.answer, ignoreCase = true) &&
              existingKey.scoreWeight == newKey.scoreWeight
    }
  }

  /**
   * Alternative method untuk merge by ID
   */
  fun mergeWithExistingDataById(
    existingId: Long,
    newTitle: String,
    newHasilKoreksi: List<HasilKoreksi>
  ) {
    viewModelScope.launch {
      try {
        _isLoading.value = true

        val existingHistory = savedScoreHistoryUseCase.getSavedScoreHistoryById(existingId)
        if (existingHistory != null) {
          mergeWithExistingData(existingHistory, newTitle, newHasilKoreksi)
        } else {
          _errorMessage.value = "Data tidak ditemukan"
        }
      } catch (e: Exception) {
        _errorMessage.value = "Gagal mengambil data existing: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistory) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        savedScoreHistoryUseCase.updateSavedScoreHistory(savedScoreHistory)
        getAllSavedScoreHistory()
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal mengupdate data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistory) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        savedScoreHistoryUseCase.deleteSavedScoreHistory(savedScoreHistory)
        getAllSavedScoreHistory()
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal menghapus data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  fun deleteSavedScoreHistoryById(id: Long) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        savedScoreHistoryUseCase.deleteSavedScoreHistoryById(id)
        getAllSavedScoreHistory()
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal menghapus data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  fun getSavedScoreHistoryById(id: Long) {
    viewModelScope.launch {
      try {
        _isLoading.value = true
        val data = savedScoreHistoryUseCase.getSavedScoreHistoryById(id)
        _savedScoreHistory.value = data
        _errorMessage.value = null
      } catch (e: Exception) {
        _errorMessage.value = "Gagal memuat detail data: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * Method untuk mendapatkan statistik ringkas dari history
   */
  fun getHistoryStats(history: SavedScoreHistory): Triple<Int, Double, String> {
    val totalStudents = history.hasilKoreksi.sumOf { it.resultData.size }
    val allScores = history.hasilKoreksi.flatMap { hasil ->
      hasil.resultData.map { siswa -> siswa.skorAkhir }
    }
    val averageScore = if (allScores.isNotEmpty()) allScores.average() else 0.0
    val lastUpdated = formatDate(history.createdAt)

    return Triple(totalStudents, averageScore, lastUpdated)
  }

  /**
   * Method untuk validasi sebelum merge
   */
  fun validateMergeData(
    existingHistory: SavedScoreHistory,
    newHasilKoreksi: List<HasilKoreksi>
  ): Pair<Boolean, String> {
    val allExistingNames = existingHistory.hasilKoreksi.flatMap { hasil ->
      hasil.resultData.map { siswa -> siswa.nama.lowercase() }
    }.toSet()

    val allNewNames = newHasilKoreksi.flatMap { hasil ->
      hasil.resultData.map { siswa -> siswa.nama.lowercase() }
    }

    val duplicateNames = allNewNames.filter { newName ->
      allExistingNames.contains(newName)
    }.toSet()

    return if (duplicateNames.isNotEmpty()) {
      false to "Ditemukan nama siswa yang sudah ada: ${duplicateNames.joinToString(", ")}"
    } else {
      true to "Data valid untuk digabungkan"
    }
  }

  fun getMergePreview(
    existingHistory: SavedScoreHistory,
    newHasilKoreksi: List<HasilKoreksi>
  ): String {
    val currentStudentCount = existingHistory.hasilKoreksi.sumOf { it.resultData.size }
    val newStudentCount = newHasilKoreksi.sumOf { it.resultData.size }
    val totalAfterMerge = currentStudentCount + newStudentCount

    return "Saat ini: $currentStudentCount siswa → Setelah digabung: $totalAfterMerge siswa"
  }

  fun clearError() {
    _errorMessage.value = null
  }

  private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale("id", "ID"))
    return sdf.format(java.util.Date(timestamp))
  }
}