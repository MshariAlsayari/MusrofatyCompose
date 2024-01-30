package com.msharialsayari.musrofaty.business_layer.data_layer.hilt


import com.msharialsayari.musrofaty.business_layer.data_layer.categories.CategoryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIServiceModule {

    @Singleton
    @Provides
    fun provideCategoryApiService(okhttpClient: OkHttpClient, converterFactory: GsonConverterFactory): CategoryApiService =
        NetworkModule.provideService(okhttpClient, converterFactory, CategoryApiService::class.java)

}