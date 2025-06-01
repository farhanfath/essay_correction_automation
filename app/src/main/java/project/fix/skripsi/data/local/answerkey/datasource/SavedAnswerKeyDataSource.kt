package project.fix.skripsi.data.local.answerkey.datasource

import project.fix.skripsi.data.local.answerkey.model.SavedAnswerKeyEntity

interface SavedAnswerKeyDataSource {
  suspend fun insertAnswerKey(answerKey: SavedAnswerKeyEntity)
  suspend fun getAllAnswerKeys(): List<SavedAnswerKeyEntity>
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKeyEntity?
  // optional - implementation
  suspend fun updateAnswerKey(answerKey: SavedAnswerKeyEntity)
  suspend fun deleteAnswerKeyById(id: Int)
}