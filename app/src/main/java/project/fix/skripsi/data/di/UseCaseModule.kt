package project.fix.skripsi.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import project.fix.skripsi.data.usecase.SavedAnswerKeyUseCaseImpl
import project.fix.skripsi.data.usecase.SavedScoreHistoryUseCaseImpl
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.repository.SavedAnswerKeyRepository
import project.fix.skripsi.domain.repository.SavedScoreHistoryRepository
import project.fix.skripsi.domain.usecase.EvaluateEssayUseCase
import project.fix.skripsi.domain.usecase.SavedAnswerKeyUseCase
import project.fix.skripsi.domain.usecase.SavedScoreHistoryUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

  @Provides
  fun provideEssayUseCase(repository: N8nRepository): EvaluateEssayUseCase {
    return EvaluateEssayUseCase(repository)
  }

  @Provides
  fun provideSavedAnswerKeyUseCase(repository: SavedAnswerKeyRepository): SavedAnswerKeyUseCase {
    return SavedAnswerKeyUseCaseImpl(repository)
  }

  @Provides
  fun provideSavedScoreHistoryUseCase(repository: SavedScoreHistoryRepository): SavedScoreHistoryUseCase {
    return SavedScoreHistoryUseCaseImpl(repository)
  }
}