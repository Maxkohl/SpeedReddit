package com.maxkohl.speedreddit.data

import com.maxkohl.speedreddit.di.RepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [RepositoryModule::class]
)
@Module
class FakeRepositoryModule {

    @Provides
    fun provideRepository(repositoryInterface: FakeAndroidTestRepository): RepositoryInterface =
        repositoryInterface
}