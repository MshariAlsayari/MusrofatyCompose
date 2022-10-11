package com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SenderDao {


    @Query("SELECT * FROM SenderEntity")
    suspend fun getAll():List<SenderEntity>


    @Query("SELECT * FROM SenderEntity WHERE id=:senderId")
    suspend fun getSenderById(senderId:Int):SenderEntity?

    @Transaction
    @Query("SELECT * FROM SenderEntity WHERE id=:senderId")
    suspend fun getSenderByIdWithSms(senderId:Int): SenderWithRelations

    @Transaction
    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    suspend fun getAllSendersWithSms(): List<SenderWithRelations>

    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    suspend fun getAllActive():List<SenderEntity>

    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    fun getActive(): Flow<List<SenderEntity>>

    @Query("SELECT * FROM SenderEntity WHERE isActive = 0")
    fun getUnActive(): Flow<List<SenderEntity>>

    @Query("UPDATE SenderEntity SET isActive =:isActive WHERE id=:senderId")
    suspend fun activeSender(senderId:Int, isActive:Boolean)

    @Query("UPDATE SenderEntity SET isPined =:isPin WHERE id=:senderId")
    suspend fun pinSender(senderId:Int, isPin:Boolean)


    @Query("UPDATE SenderEntity SET displayNameAr =:name WHERE id=:senderId")
    suspend fun updateArabicDisplayName(senderId:Int, name:String)

    @Query("UPDATE SenderEntity SET displayNameEn =:name WHERE id=:senderId")
    suspend fun updateEnglishDisplayName(senderId:Int, name:String)

    @Query("UPDATE SenderEntity SET contentId =:categoryId WHERE id=:senderId")
    suspend fun updateCategory(senderId:Int, categoryId:Int)


    @Query("UPDATE SenderEntity SET isPined =0")
    suspend fun removePinFromAll()

    @Query("DELETE FROM SenderEntity WHERE id =:senderId")
    suspend fun delete(senderId:Int)


    @Update
    suspend fun update(senderEntity: SenderEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg senderEntity: SenderEntity)
}