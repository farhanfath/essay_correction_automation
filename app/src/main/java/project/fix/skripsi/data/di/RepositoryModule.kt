package project.fix.skripsi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.fix.skripsi.data.local.datasource.SavedAnswerKeyDataSource
import project.fix.skripsi.data.local.datasource.SavedScoreHistoryDataSource
import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSource
import project.fix.skripsi.data.repository.N8nRepositoryImpl
import project.fix.skripsi.data.repository.SavedAnswerKeyRepositoryImpl
import project.fix.skripsi.data.repository.SavedScoreHistoryRepositoryImpl
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.repository.SavedAnswerKeyRepository
import project.fix.skripsi.domain.repository.SavedScoreHistoryRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
  @Provides
  fun provideN8nRepository(n8nDataSource: N8nDataSource): N8nRepository {
    return N8nRepositoryImpl(n8nDataSource)
  }

  @Provides
  fun provideSavedAnswerKeyRepository(savedAnswerKeyDataSource: SavedAnswerKeyDataSource): SavedAnswerKeyRepository {
    return SavedAnswerKeyRepositoryImpl(savedAnswerKeyDataSource)
  }

  @Provides
  fun provideSavedScoreHistoryRepository(
    savedScoreHistoryDataSource: SavedScoreHistoryDataSource
  ): SavedScoreHistoryRepository {
    return SavedScoreHistoryRepositoryImpl(savedScoreHistoryDataSource)
  }
}