package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface StoreDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg storeEntity: StoreEntity)


    @Query("SELECT * FROM StoreEntity WHERE LOWER(storeName) = LOWER(:storeName)")
    suspend fun getStoreByName(storeName: String): StoreEntity?


    @Transaction
    @Query("SELECT * FROM StoreEntity WHERE storeName LIKE '%' || :storeName || '%' ")
     fun getAll(storeName: String): Flow<List<StoreWithCategory>>

    @Update
    suspend fun update(storeEntity: StoreEntity)
}