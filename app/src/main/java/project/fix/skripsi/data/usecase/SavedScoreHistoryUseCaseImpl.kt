package project.fix.skripsi.data.usecase

import kotlinx.coroutines.flow.Flow
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.repository.SavedScoreHistoryRepository
import project.fix.skripsi.domain.usecase.SavedScoreHistoryUseCase
import javax.inject.Inject

class SavedScoreHistoryUseCaseImpl @Inject constructor(
  private val repo: SavedScoreHistoryRepository
) : SavedScoreHistoryUseCase {
  override suspend fun getAllSavedScoreHistory(): List<SavedScoreHistory> =
    repo.getAllSavedScoreHistory()

  override suspend fun getSavedScoreHistoryById(id: Long): SavedScoreHistory? {
    return repo.getSavedScoreHistoryById(id)
  }

  override suspend fun insertSavedScoreHistory(savedScoreHistory: SavedScoreHistory) =
    repo.insertSavedScoreHistory(savedScoreHistory)

  override suspend fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistory) =
    repo.updateSavedScoreHistory(savedScoreHistory)

  override suspend fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistory) =
    repo.deleteSavedScoreHistory(savedScoreHistory)

  override suspend fun deleteSavedScoreHistoryById(id: Long) =
    repo.deleteSavedScoreHistoryById(id)

}