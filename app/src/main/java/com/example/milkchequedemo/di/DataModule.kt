package com.example.milkchequedemo.di

import com.example.milkchequedemo.data.datasource.RemoteDataSource
import com.example.milkchequedemo.data.datasource.RemoteDataSourceImpl
import com.example.milkchequedemo.data.repositoryImpl.StoreRepositoryImpl
import com.example.milkchequedemo.domain.repository.StoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds @Singleton
    abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

    @Binds @Singleton
    abstract fun bindStoreRepository(impl: StoreRepositoryImpl): StoreRepository

    @Binds @Singleton
    abstract fun bindMenuRepository(impl: com.example.milkchequedemo.data.repositoryImpl.MenuRepositoryImpl)
            : com.example.milkchequedemo.domain.repository.MenuRepository
}
