package project.fix.skripsi.data.local.datasource

import project.fix.skripsi.data.local.dao.SavedAnswerKeyDao
import project.fix.skripsi.data.local.model.SavedAnswerKeyEntity
import javax.inject.Inject

class SavedAnswerKeyDataSourceImpl @Inject constructor(
  private val savedAnswerKeyDao: SavedAnswerKeyDao
) : SavedAnswerKeyDataSource {
  override suspend fun insertAnswerKey(answerKey: SavedAnswerKeyEntity) {
    savedAnswerKeyDao.insertAnswerKey(answerKey)
  }

  override suspend fun getAllAnswerKeys(): List<SavedAnswerKeyEntity> {
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