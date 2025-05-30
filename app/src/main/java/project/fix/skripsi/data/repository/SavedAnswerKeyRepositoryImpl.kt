package project.fix.skripsi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import project.fix.skripsi.data.local.answerkey.datasource.SavedAnswerKeyDataSource
import project.fix.skripsi.data.local.answerkey.model.SavedAnswerKeyEntity
import project.fix.skripsi.data.local.answerkey.model.toEntity
import project.fix.skripsi.domain.model.SavedAnswerKey
import project.fix.skripsi.domain.repository.SavedAnswerKeyRepository
import javax.inject.Inject

class SavedAnswerKeyRepositoryImpl @Inject constructor(
  private val savedAnswerKeyDataSource: SavedAnswerKeyDataSource
) : SavedAnswerKeyRepository {
  override suspend fun insertAnswerKey(answerKey: SavedAnswerKey) =
    savedAnswerKeyDataSource.insertAnswerKey(answerKey.toEntity())

  override fun getAllAnswerKeys(): Flow<List<SavedAnswerKey>> {
    return Pager(config = PagingConfig(pageSize = 10)) {
      savedAnswerKeyDataSource.getAllAnswerKeys()
    }.flow.map { pagingData ->
      val savedAnswerKey = mutableListOf<SavedAnswerKey>()
      pagingData.map { entity ->
        val data = SavedAnswerKeyEntity.toDomain(entity)
        savedAnswerKey.add(data)
      }
      savedAnswerKey
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