package project.fix.skripsi.data.repository

import project.fix.skripsi.data.local.datasource.SavedAnswerKeyDataSource
import project.fix.skripsi.data.local.model.SavedAnswerKeyEntity
import project.fix.skripsi.data.local.model.toEntity
import project.fix.skripsi.domain.model.SavedAnswerKey
import project.fix.skripsi.domain.repository.SavedAnswerKeyRepository
import javax.inject.Inject

class SavedAnswerKeyRepositoryImpl @Inject constructor(
  private val savedAnswerKeyDataSource: SavedAnswerKeyDataSource
) : SavedAnswerKeyRepository {
  override suspend fun insertAnswerKey(answerKey: SavedAnswerKey) =
    savedAnswerKeyDataSource.insertAnswerKey(answerKey.toEntity())

  override suspend fun getAllAnswerKeys(): List<SavedAnswerKey> {
    return savedAnswerKeyDataSource.getAllAnswerKeys().map {
      SavedAnswerKeyEntity.toDomain(it)
    }
  }


  override suspend fun getAnswerKeyById(id: Int): SavedAnswerKey? {
    return savedAnswerKeyDataSource.getAnswerKeyById(id)?.let {
      SavedAnswerKeyEntity.toDomain(it)
    }
  }

  override suspend fun updateAnswerKey(answerKey: SavedAnswerKey) =
    savedAnswerKeyDataSource.updateAnswerKey(answerKey.toEntity())

  override suspend fun deleteAnswerKeyById(id: Int) =
    savedAnswerKeyDataSource.deleteAnswerKeyById(id)
}