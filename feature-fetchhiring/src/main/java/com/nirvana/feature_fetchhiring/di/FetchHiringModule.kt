package com.nirvana.feature_fetchhiring.di

import com.nirvana.feature_fetchhiring.BuildConfig
import com.nirvana.feature_fetchhiring.repository.HiringRepository
import com.nirvana.feature_fetchhiring.repository.HiringRepositoryImpl
import com.nirvana.feature_fetchhiring.repository.HiringService
import com.nirvana.feature_fetchhiring.usecase.FetchHiringUseCase
import com.nirvana.feature_fetchhiring.usecase.FetchHiringUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FetchHiringModule {
    companion object {
        @Provides
        @Singleton
        @Named("BASE_URL")
        fun provideBaseUrl(): String = BuildConfig.BASE_URL

        @Provides
        @Singleton
        fun provideHiringService(retrofit: Retrofit): HiringService =
            retrofit.create(HiringService::class.java)
    }

    @Binds
    abstract fun bindHiringRepository(repository: HiringRepositoryImpl): HiringRepository

    @Binds
    abstract fun bindFetchUseCase(fetchHiringUseCaseImpl: FetchHiringUseCaseImpl): FetchHiringUseCase
}