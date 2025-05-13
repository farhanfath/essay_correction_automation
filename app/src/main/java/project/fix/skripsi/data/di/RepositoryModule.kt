package project.fix.skripsi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSource
import project.fix.skripsi.data.repository.N8nRepositoryImpl
import project.fix.skripsi.domain.repository.N8nRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
  @Provides
  fun provideN8nRepository(n8nDataSource: N8nDataSource): N8nRepository {
    return N8nRepositoryImpl(n8nDataSource)
  }
}