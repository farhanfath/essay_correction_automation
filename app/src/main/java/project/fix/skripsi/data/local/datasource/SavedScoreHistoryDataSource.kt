package project.fix.skripsi.data.local.datasource

import kotlinx.coroutines.flow.Flow
import project.fix.skripsi.data.local.model.SavedScoreHistoryEntity

interface SavedScoreHistoryDataSource {
  suspend fun insertSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity)
  suspend fun getAllSavedScoreHistory(): List<SavedScoreHistoryEntity>
  suspend fun getSavedScoreHistoryById(id: Long): SavedScoreHistoryEntity?

  suspend fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity)
  suspend fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity)
  suspend fun deleteSavedScoreHistoryById(id: Long)
}