package project.fix.skripsi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import project.fix.skripsi.data.local.converter.Converters
import project.fix.skripsi.data.local.dao.SavedScoreHistoryDao
import project.fix.skripsi.data.local.dao.SavedAnswerKeyDao
import project.fix.skripsi.data.local.model.SavedScoreHistoryEntity
import project.fix.skripsi.data.local.model.SavedAnswerKeyEntity

@Database(
  entities = [SavedAnswerKeyEntity::class, SavedScoreHistoryEntity::class],
  version = 2,
  exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun savedAnswerKeyDao(): SavedAnswerKeyDao
  abstract fun savedScoreHistoryDao(): SavedScoreHistoryDao
}