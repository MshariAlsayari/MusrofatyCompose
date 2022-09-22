package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SmsDao {



    @Query("SELECT * FROM SmsEntity WHERE LOWER(senderName)  = LOWER(:senderName)")
    suspend fun getSmsBySenderName(senderName:String): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE id = :id")
    suspend fun getSms(id:String): SmsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg smsEntity: SmsEntity)

}