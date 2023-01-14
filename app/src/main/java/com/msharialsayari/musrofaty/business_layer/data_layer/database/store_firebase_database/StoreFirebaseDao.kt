package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity

@Dao
interface StoreFirebaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg storeFirebaseEntity: StoreFirebaseEntity)

    @Query("SELECT * FROM StoreFirebaseEntity WHERE LOWER(name) = LOWER(:storeName)")
    suspend fun getStoreByName(storeName: String): StoreFirebaseEntity?
}