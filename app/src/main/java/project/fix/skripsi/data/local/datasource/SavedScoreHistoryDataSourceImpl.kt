package project.fix.skripsi.data.local.datasource

import project.fix.skripsi.data.local.dao.SavedScoreHistoryDao
import project.fix.skripsi.data.local.model.SavedScoreHistoryEntity
import javax.inject.Inject

class SavedScoreHistoryDataSourceImpl @Inject constructor(
  private val dao: SavedScoreHistoryDao
) : SavedScoreHistoryDataSource {
  override suspend fun insertSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity) =
    dao.insertSavedScoreHistory(savedScoreHistory)

  override suspend fun getAllSavedScoreHistory(): List<SavedScoreHistoryEntity> = dao.getAllSavedScoreHistory()

  override suspend fun getSavedScoreHistoryById(id: Long): SavedScoreHistoryEntity? = dao.getSavedScoreHistoryById(id)

  override suspend fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity) =
    dao.updateSavedScoreHistory(savedScoreHistory)

  override suspend fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity) =
    dao.deleteSavedScoreHistory(savedScoreHistory)

  override suspend fun deleteSavedScoreHistoryById(id: Long) = dao.deleteSavedScoreHistoryById(id)
}