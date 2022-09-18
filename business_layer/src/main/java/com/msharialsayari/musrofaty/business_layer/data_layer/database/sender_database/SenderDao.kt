package com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database

import androidx.room.*

@Dao
interface SenderDao {


    @Query("SELECT * FROM SenderEntity")
    suspend fun getAll():List<SenderEntity>

    @Transaction
    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    suspend fun getAllSendersWithSms(): List<SenderWithRelations>

    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    suspend fun getAllActive():List<SenderEntity>

    @Query("UPDATE SenderEntity SET isActive =:isActive WHERE senderName=:senderName")
    suspend fun activeSender(senderName:String, isActive:Boolean)

    @Query("UPDATE SenderEntity SET isPined =:isPin WHERE senderName=:senderName")
    suspend fun pinSender(senderName:String, isPin:Boolean)

    @Query("UPDATE SenderEntity SET isPined =0")
    suspend fun removePinFromAll()


    @Update
    suspend fun update(senderEntity: SenderEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg senderEntity: SenderEntity)
}