package project.fix.skripsi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import project.fix.skripsi.data.local.answerkey.converter.SavedAnswerKeyConverter
import project.fix.skripsi.data.local.answerkey.dao.SavedAnswerKeyDao
import project.fix.skripsi.data.local.answerkey.model.SavedAnswerKeyEntity

@Database(entities = [SavedAnswerKeyEntity::class], version = 1, exportSchema = false)
@TypeConverters(SavedAnswerKeyConverter::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun savedAnswerKeyDao(): SavedAnswerKeyDao
}