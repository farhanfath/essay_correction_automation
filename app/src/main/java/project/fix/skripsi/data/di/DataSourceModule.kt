package project.fix.skripsi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.fix.skripsi.data.local.answerkey.dao.SavedAnswerKeyDao
import project.fix.skripsi.data.local.answerkey.datasource.SavedAnswerKeyDataSource
import project.fix.skripsi.data.local.answerkey.datasource.SavedAnswerKeyDataSourceImpl
import project.fix.skripsi.data.remote.n8n.N8nApiService
import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSource
import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
  @Provides
  fun provideN8nDataSource(n8nApiService: N8nApiService): N8nDataSource {
    return N8nDataSourceImpl(n8nApiService)
  }

  @Provides
  fun provideSavedAnswerKeyDataSource(savedAnswerKeyDao: SavedAnswerKeyDao): SavedAnswerKeyDataSource {
    return SavedAnswerKeyDataSourceImpl(savedAnswerKeyDao)
  }
}