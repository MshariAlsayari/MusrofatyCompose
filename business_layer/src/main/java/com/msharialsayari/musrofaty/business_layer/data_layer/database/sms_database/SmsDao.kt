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

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodaySms(senderId:Int): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekSms(senderId:Int): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthSms(senderId:Int): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearSms(senderId:Int): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%d-%m-%Y', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%d-%m-%Y', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateSms(senderId:Int, startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>



    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId")
     fun getSmsBySenderId(senderId:Int): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite")
    fun getAllFavoriteSms(senderId:Int, isFavorite: Boolean = true): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodayFavoriteSms(senderId:Int, isFavorite: Boolean = true): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekFavoriteSms(senderId:Int, isFavorite: Boolean = true): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthFavoriteSms(senderId:Int, isFavorite: Boolean = true): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearFavoriteSms(senderId:Int, isFavorite: Boolean = true): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%d-%m-%Y', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%d-%m-%Y', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateFavoriteSms(senderId:Int, isFavorite: Boolean = true, startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>

}