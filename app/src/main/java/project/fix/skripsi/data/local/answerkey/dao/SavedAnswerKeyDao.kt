package project.fix.skripsi.data.local.answerkey.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import project.fix.skripsi.data.local.answerkey.model.SavedAnswerKeyEntity

@Dao
interface SavedAnswerKeyDao {
  @Insert
  suspend fun insertAnswerKey(answerKey: SavedAnswerKeyEntity)

  @Query("SELECT * FROM essay_answer_key")
  fun getAllAnswerKeys(): PagingSource<Int, SavedAnswerKeyEntity>

  @Query("SELECT * FROM essay_answer_key WHERE id = :id")
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKeyEntity?

  // optional - implementation
  @Update
  suspend fun updateAnswerKey(answerKey: SavedAnswerKeyEntity)

  @Query("DELETE FROM essay_answer_key WHERE id = :id")
  suspend fun deleteAnswerKeyById(id: Int)
}