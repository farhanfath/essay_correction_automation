package project.fix.skripsi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

  @Provides
  fun provideEssayUseCase(repository: N8nRepository): EvaluateEssayUseCase {
    return EvaluateEssayUseCase(repository)
  }
}