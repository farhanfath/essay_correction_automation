package project.fix.skripsi.domain.repository

import project.fix.skripsi.domain.model.SavedScoreHistory

interface SavedScoreHistoryRepository {
  suspend fun getAllSavedScoreHistory(): List<SavedScoreHistory>
  suspend fun getSavedScoreHistoryById(id: Long): SavedScoreHistory?
  suspend fun insertSavedScoreHistory(savedScoreHistory: SavedScoreHistory)
  suspend fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistory)
  suspend fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistory)
  suspend fun deleteSavedScoreHistoryById(id: Long)
}