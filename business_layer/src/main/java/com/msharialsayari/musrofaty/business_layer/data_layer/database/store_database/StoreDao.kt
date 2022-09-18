package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database

import androidx.room.*


@Dao
interface StoreDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg storeEntity: StoreEntity)


    @Query("SELECT * FROM StoreEntity WHERE storeName = :storeName")
    suspend fun getStoreByName(storeName: String): StoreEntity?

    @Update
    suspend fun update(storeEntity: StoreEntity)
}