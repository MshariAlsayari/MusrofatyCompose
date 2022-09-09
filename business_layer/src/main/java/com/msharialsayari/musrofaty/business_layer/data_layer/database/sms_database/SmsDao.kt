package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SmsDao {


    @Query("SELECT * FROM SmsEntity WHERE senderName COLLATE NOCASE IN (:senderNames) AND isDeleted = :isDeleted")
    suspend fun getAll(senderNames:List<String>,isDeleted:Boolean): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderName COLLATE NOCASE IN (:senderNames)")
    suspend fun getAllNoCheckIsDeleted(senderNames:List<String>): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE LOWER(senderName)  = LOWER(:senderName) AND isDeleted = :isDeleted")
    suspend fun getSmsBySenderName(senderName:String,isDeleted:Boolean): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE LOWER(senderName)  = LOWER(:senderName)")
    suspend fun getSmsBySenderNameNoCheckIsDeleted(senderName:String): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE id = :id")
    suspend fun getSms(id:String): SmsEntity?

    @Query("UPDATE SmsEntity SET isDeleted = :isDeleted WHERE id IN (:list)")
    suspend fun setDeletedSms( list: List<String>, isDeleted: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg smsEntity: SmsEntity)

}