package project.fix.skripsi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import project.fix.skripsi.data.local.model.SavedAnswerKeyEntity

@Dao
interface SavedAnswerKeyDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnswerKey(answerKey: SavedAnswerKeyEntity)

  @Query("SELECT * FROM essay_answer_key")
  suspend fun getAllAnswerKeys(): List<SavedAnswerKeyEntity>

  @Query("SELECT * FROM essay_answer_key WHERE id = :id")
  suspend fun getAnswerKeyById(id: Int): SavedAnswerKeyEntity?

  // optional - implementation
  @Update
  suspend fun updateAnswerKey(answerKey: SavedAnswerKeyEntity)

  @Query("DELETE FROM essay_answer_key WHERE id = :id")
  suspend fun deleteAnswerKeyById(id: Int)
}
