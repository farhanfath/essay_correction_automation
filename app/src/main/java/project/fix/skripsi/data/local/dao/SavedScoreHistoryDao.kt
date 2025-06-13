package project.fix.skripsi.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import project.fix.skripsi.data.local.model.SavedScoreHistoryEntity

@Dao
interface SavedScoreHistoryDao {
  @Query("SELECT * FROM saved_score_history ORDER BY createdAt DESC")
  suspend fun getAllSavedScoreHistory(): List<SavedScoreHistoryEntity>

  @Query("SELECT * FROM saved_score_history WHERE id = :id")
  suspend fun getSavedScoreHistoryById(id: Long): SavedScoreHistoryEntity?

  @Insert
  suspend fun insertSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity)

  @Update
  suspend fun updateSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity)

  @Delete
  suspend fun deleteSavedScoreHistory(savedScoreHistory: SavedScoreHistoryEntity)

  @Query("DELETE FROM saved_score_history WHERE id = :id")
  suspend fun deleteSavedScoreHistoryById(id: Long)
}