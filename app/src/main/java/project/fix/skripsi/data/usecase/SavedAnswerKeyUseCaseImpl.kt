package project.fix.skripsi.data.usecase

import kotlinx.coroutines.flow.Flow
import project.fix.skripsi.domain.model.SavedAnswerKey
import project.fix.skripsi.domain.repository.SavedAnswerKeyRepository
import project.fix.skripsi.domain.usecase.SavedAnswerKeyUseCase
import javax.inject.Inject

class SavedAnswerKeyUseCaseImpl @Inject constructor(
  private val repo: SavedAnswerKeyRepository
) : SavedAnswerKeyUseCase {
  override suspend fun insertAnswerKey(answerKey: SavedAnswerKey) = repo.insertAnswerKey(answerKey)

  override suspend fun getAllAnswerKeys(): Flow<List<SavedAnswerKey>> = repo.getAllAnswerKeys()

  override suspend fun getAnswerKeyById(id: Int): SavedAnswerKey? = repo.getAnswerKeyById(id)

  override suspend fun updateAnswerKey(answerKey: SavedAnswerKey) = repo.updateAnswerKey(answerKey)

  override suspend fun deleteAnswerKeyById(id: Int) = repo.deleteAnswerKeyById(id)
}