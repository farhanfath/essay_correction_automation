package project.fix.skripsi.data.local.answerkey.datasource

import androidx.paging.PagingSource
import project.fix.skripsi.data.local.answerkey.model.SavedAnswerKeyEntity
import project.fix.skripsi.domain.model.SavedAnswerKey

interface SavedAnswerKeyDataSource {
  suspend fun insertAnswerKey(answerKey: SavedAnswerKeyEntity)
  fun getAllAnswerKeys(): PagingSource<Int, SavedAnswerKeyEntity>
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKeyEntity?
  // optional - implementation
  suspend fun updateAnswerKey(answerKey: SavedAnswerKeyEntity)
  suspend fun deleteAnswerKeyById(id: Int)
}