package project.fix.skripsi.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.SavedAnswerKey

@Entity(tableName = "essay_answer_key")
data class SavedAnswerKeyEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val title: String,
  val createdAt: Long = System.currentTimeMillis(),
  val answerKeys: List<AnswerKeyItem> = emptyList()
) {
  companion object {
    fun toDomain(entity: SavedAnswerKeyEntity): SavedAnswerKey {
      return SavedAnswerKey(
        id = entity.id,
        title = entity.title,
        createdAt = entity.createdAt,
        answerKeys = entity.answerKeys
      )
    }
  }
}

fun SavedAnswerKey.toEntity(): SavedAnswerKeyEntity {
  return SavedAnswerKeyEntity(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    answerKeys = this.answerKeys
  )
}