package com.example.milkchequedemo.di


import com.example.milkchequedemo.data.service.StoreApi
import com.example.milkchequedemo.data.service.StoreApiProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkBridgeModule {
    @Provides @Singleton
    fun provideStoreApi(): StoreApi = StoreApiProvider.api
}
