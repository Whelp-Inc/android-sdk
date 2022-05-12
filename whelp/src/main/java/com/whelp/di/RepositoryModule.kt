package com.whelp.di

import com.whelp.data.ApiService
import com.whelp.data.AuthRepository
import com.whelp.data.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideDiscoverRepository(
        apiService: ApiService
    ): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }
}