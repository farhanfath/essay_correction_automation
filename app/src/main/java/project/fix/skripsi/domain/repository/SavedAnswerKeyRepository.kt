package project.fix.skripsi.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import project.fix.skripsi.domain.model.SavedAnswerKey

interface SavedAnswerKeyRepository {
  suspend fun insertAnswerKey(answerKey: SavedAnswerKey)
  fun getAllAnswerKeys(): Flow<List<SavedAnswerKey>>
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKey?
  suspend fun updateAnswerKey(answerKey: SavedAnswerKey)
  suspend fun deleteAnswerKeyById(id: Int)
}