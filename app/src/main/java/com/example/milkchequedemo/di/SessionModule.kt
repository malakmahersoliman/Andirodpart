package com.example.milkchequedemo.di

import com.example.milkchequedemo.data.datasource.RemoteDataSource
import com.example.milkchequedemo.data.repositoryImpl.SessionRepositoryImpl
import com.example.milkchequedemo.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SessionModule {
    @Provides @Singleton
    fun provideSessionRepository(
        remote: RemoteDataSource
    ): SessionRepository= SessionRepositoryImpl(
        remote = remote
    )
}