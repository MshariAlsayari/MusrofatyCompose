package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SmsDao {



    @Query("SELECT * FROM SmsEntity WHERE LOWER(senderName)  = LOWER(:senderName)")
    suspend fun getSmsBySenderName(senderName:String): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE id = :id")
    suspend fun getSms(id:String): SmsEntity?

    @Query("UPDATE SmsEntity SET isFavorite =:favorite WHERE id=:id")
    suspend fun favoriteSms(id:String, favorite:Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg smsEntity: SmsEntity)

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId")
     fun getAllSms(senderId:Int): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND timestamp BETWEEN :startDay AND :endDay ")
    fun getAllSms(senderId:Int, startDay:Long,  endDay:Long): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId")
     fun getSmsBySenderId(senderId:Int): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite")
    fun getAllFavoriteSms(senderId:Int, isFavorite: Boolean = true): PagingSource<Int,SmsEntity>

}