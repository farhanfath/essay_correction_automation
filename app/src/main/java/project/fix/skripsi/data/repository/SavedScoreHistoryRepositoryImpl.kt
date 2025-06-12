package project.fix.skripsi.data.repository

import project.fix.skripsi.data.local.datasource.SavedScoreHistoryDataSource
import project.fix.skripsi.data.local.model.SavedScoreHistoryEntity
import project.fix.skripsi.data.local.model.toEntity
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.repository.SavedScoreHistoryRepository
import javax.inject.Inject

class SavedScoreHistoryRepositoryImpl @Inject constructor(
  private val dataSource: SavedScoreHistoryDataSource
) : SavedScoreHistoryRepository {
  override suspend fun getAllSavedScoreHistory(): List<SavedScoreHistory> {
    return dataSource.getAllSavedScoreHistory().map {
      SavedScoreHistoryEntity.toDomain(it)
    }
  }

  override suspend fun getSavedScoreHistoryById(id: Long): SavedScoreHistory? {
    return dataSource.getSavedScoreHistoryById(id)?.let {
      SavedScoreHistoryEntity.toDomain(it)
    }
  }

  override suspend fun insertSavedScoreHistory(savedScoreHistory: SavedScoreHistory) =
    dataSource.insertSavedScoreHistory(savedScoreHistory.toEntity())

  override suspend fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistory) =
    dataSource.updateSavedScoreHistory(savedScoreHistory.toEntity())

  override suspend fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistory) =
    dataSource.deleteSavedScoreHistory(savedScoreHistory.toEntity())

  override suspend fun deleteSavedScoreHistoryById(id: Long) =
    dataSource.deleteSavedScoreHistoryById(id)
}