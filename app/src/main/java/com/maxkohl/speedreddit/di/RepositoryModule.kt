package com.maxkohl.speedreddit.di

import com.maxkohl.speedreddit.data.MainRepository
import com.maxkohl.speedreddit.data.RepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(
        repositoryInterface: MainRepository
    ): RepositoryInterface = repositoryInterface
}