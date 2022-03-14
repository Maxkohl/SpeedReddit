package com.maxkohl.speedreddit.di

import com.maxkohl.speedreddit.data.RedditApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Named("BaseUrl")
    fun providesBaseUrl(): String {
        return "https://reddit.com/r/"
    }

    @Singleton
    @Provides
    @Named("Retrofit")
    fun provideRetrofit(@Named("BaseUrl") baseUrl: String): Retrofit =
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
            baseUrl
        ).build()

    @Singleton
    @Provides
    @Named("RedditApiService")
    fun providesApiService(@Named("Retrofit") retrofit: Retrofit): RedditApiService =
        retrofit.create(RedditApiService::class.java)

}