package project.fix.skripsi.domain.usecase

import kotlinx.coroutines.flow.Flow
import project.fix.skripsi.domain.model.SavedAnswerKey

interface SavedAnswerKeyUseCase {
  suspend fun insertAnswerKey(answerKey: SavedAnswerKey)
  suspend fun getAllAnswerKeys(): Flow<List<SavedAnswerKey>>
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKey?
  suspend fun updateAnswerKey(answerKey: SavedAnswerKey)
  suspend fun deleteAnswerKeyById(id: Int)
}