package project.fix.skripsi.data.local.answerkey.datasource

import androidx.paging.PagingSource
import project.fix.skripsi.data.local.answerkey.dao.SavedAnswerKeyDao
import project.fix.skripsi.data.local.answerkey.model.SavedAnswerKeyEntity
import javax.inject.Inject

class SavedAnswerKeyDataSourceImpl @Inject constructor(
  private val savedAnswerKeyDao: SavedAnswerKeyDao
) : SavedAnswerKeyDataSource {
  override suspend fun insertAnswerKey(answerKey: SavedAnswerKeyEntity) {
    savedAnswerKeyDao.insertAnswerKey(answerKey)
  }

  override fun getAllAnswerKeys(): PagingSource<Int, SavedAnswerKeyEntity> {
    return savedAnswerKeyDao.getAllAnswerKeys()
  }

  override suspend fun getAnswerKeyById(id: Int): SavedAnswerKeyEntity? {
    return savedAnswerKeyDao.getAnswerKeyById(id)
  }

  override suspend fun updateAnswerKey(answerKey: SavedAnswerKeyEntity) {
    savedAnswerKeyDao.updateAnswerKey(answerKey)
  }

  override suspend fun deleteAnswerKeyById(id: Int) {
    savedAnswerKeyDao.deleteAnswerKeyById(id)
  }
}