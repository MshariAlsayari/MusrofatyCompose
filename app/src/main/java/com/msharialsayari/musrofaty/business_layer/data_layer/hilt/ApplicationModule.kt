package com.msharialsayari.musrofaty.business_layer.data_layer.hilt

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.business_layer.data_layer.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [DataSourceModule::class])
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, AppDatabase::class.java, "AppDatabase")
            .addMigrations(
                MIGRATION_1_2,
                FILTER_MIGRATION_2_1,
                FILTER_MIGRATION_3_4,
                SMS_CATEGORY_MIGRATION_4_5,
                SMS_DELETE_FLAG_MIGRATION_5_6,
                FILTER_DATE_FROM_MIGRATION_6_7,
                MIGRATION_9_10,
                MIGRATION_10_11,
                MIGRATION_11_12,
                MIGRATION_12_13,
                MIGRATION_13_14,
                MIGRATION_14_15,
                MIGRATION_15_16,
                MIGRATION_16_17,
                MIGRATION_17_18,
                MIGRATION_18_19,
                MIGRATION_19_20,
                MIGRATION_20_21,
                MIGRATION_21_22,
                MIGRATION_22_23,
                MIGRATION_23_24,
                MIGRATION_24_25,
                MIGRATION_25_26,
                MIGRATION_26_27,
                MIGRATION_27_28,
                MIGRATION_28_29

            )
            .build()


    @Singleton
    @Provides
    fun provideWorManager(@ApplicationContext  context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideSmsDao(db: AppDatabase) = db.smmDao()


    @Singleton
    @Provides
    fun provideFilterDao(db: AppDatabase) = db.filterDao()

    @Singleton
    @Provides
    fun provideFilterAdvancedDao(db: AppDatabase) = db.filterAdvancedDao()

    @Singleton
    @Provides
    fun provideCategoryDto(db: AppDatabase) = db.categoryDto()

    @Singleton
    @Provides
    fun provideStoreDto(db: AppDatabase) = db.storeDao()

    @Singleton
    @Provides
    fun provideWordDto(db: AppDatabase) = db.wordDto()

    @Singleton
    @Provides
    fun provideSenderDto(db: AppDatabase) = db.senderDao()

    @Singleton
    @Provides
    fun provideContentDto(db: AppDatabase) = db.contentDao()

    @Singleton
    @Provides
    fun provideStoreFirebaseDto(db: AppDatabase) = db.storeFirebaseDao()



}