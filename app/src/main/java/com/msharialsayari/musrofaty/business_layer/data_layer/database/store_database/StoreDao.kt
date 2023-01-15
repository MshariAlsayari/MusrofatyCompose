package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface StoreDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg storeEntity: StoreEntity)


    @Query("SELECT * FROM StoreEntity WHERE LOWER(name) = LOWER(:storeName)")
    suspend fun getStoreByName(storeName: String): StoreEntity?

    @Query("SELECT * FROM StoreEntity WHERE category_id = :id")
    fun getStoreByCategoryId(id: Int): Flow<List<StoreEntity>>


    @Transaction
    @Query("SELECT * FROM StoreEntity WHERE name LIKE '%' || :storeName || '%' ")
     fun getAll(storeName: String): Flow<List<StoreWithCategory>>

    @Update
    suspend fun update(storeEntity: StoreEntity)
}