package project.fix.skripsi.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import project.fix.skripsi.data.local.AppDatabase
import project.fix.skripsi.data.local.dao.SavedScoreHistoryDao
import project.fix.skripsi.data.local.dao.SavedAnswerKeyDao

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
  @Provides
  fun provideDatabase(@ApplicationContext context : Context) : AppDatabase {
    return Room.databaseBuilder(
      context,
      AppDatabase::class.java,
      "local_database"
    ).build()
  }

  @Provides
  fun provideSavedAnswerKeyDao(database: AppDatabase): SavedAnswerKeyDao {
    return database.savedAnswerKeyDao()
  }

  @Provides
  fun provideSavedScoreHistoryDao(database: AppDatabase): SavedScoreHistoryDao {
    return database.savedScoreHistoryDao()
  }
}