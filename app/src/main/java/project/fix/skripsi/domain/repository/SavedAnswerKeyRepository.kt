package project.fix.skripsi.domain.repository

import project.fix.skripsi.domain.model.SavedAnswerKey

interface SavedAnswerKeyRepository {
  suspend fun insertAnswerKey(answerKey: SavedAnswerKey)
  suspend fun getAllAnswerKeys(): List<SavedAnswerKey>
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKey?
  suspend fun updateAnswerKey(answerKey: SavedAnswerKey)
  suspend fun deleteAnswerKeyById(id: Int)
}