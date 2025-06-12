package project.fix.skripsi.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory

@Entity(tableName = "saved_score_history")
data class SavedScoreHistoryEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val hasilKoreksi: List<HasilKoreksi>,
  val createdAt: Long = System.currentTimeMillis()
) {
  companion object {
    fun toDomain(entity: SavedScoreHistoryEntity): SavedScoreHistory {
      return SavedScoreHistory(
        id = entity.id,
        title = entity.title,
        hasilKoreksi = entity.hasilKoreksi,
        createdAt = entity.createdAt
      )
    }
  }
}

fun SavedScoreHistory.toEntity(): SavedScoreHistoryEntity {
  return SavedScoreHistoryEntity(
    id = this.id,
    title = this.title,
    hasilKoreksi = this.hasilKoreksi,
    createdAt = this.createdAt
  )
}