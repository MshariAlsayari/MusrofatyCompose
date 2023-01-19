package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SmsDao {





    @Query("SELECT * FROM SmsEntity WHERE id = :id")
    suspend fun getSms(id:String): SmsEntity?

    @Query("UPDATE SmsEntity SET isFavorite =:favorite WHERE id=:id")
    suspend fun favoriteSms(id:String, favorite:Boolean)

    @Query("UPDATE SmsEntity SET isDeleted =:delete WHERE id=:id")
    suspend fun softDelete(id:String, delete:Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg smsEntity: SmsEntity)


    ////////Dashboard///////////
    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%'")
     fun getAllDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
     fun getTodayDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
     fun getCurrentWeekDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
     fun getCurrentMonthDashboard(query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
     fun getCurrentYearDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
     fun getRangeDateDashboard(query:String="", startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>



    ////////Statistics///////////

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%'")
    suspend fun getAll(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    suspend fun getToday(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    suspend fun getCurrentWeek(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    suspend fun getCurrentMonth(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    suspend fun getCurrentYear(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
    suspend fun getRangeDate(senderId:Int, query:String="", isDeleted: Boolean=false, startDate:Long, endDate:Long): List<SmsEntity>

    ////////Generating file and for sender sms list screen///////////

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%'")
     fun getAllSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodaySms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateSms(senderId:Int, query:String="", isDeleted: Boolean=false, startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>


    ///////Favorite/////////
    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%'")
    fun getAllFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodayFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String="", startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>


    //////Soft deleted////////
    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isFavorite AND body LIKE '%' || :query || '%'")
    fun getAllSoftDeletedSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodaySoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String="", startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%'")
    fun getSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodaySmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateSmsBySenderId(senderId:Int, query:String="", startDate:Long, endDate:Long): Flow<List<SmsEntity>>


    //////Check soft Delete//////
    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%'")
    fun getSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))")
    fun getTodaySmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))")
    fun getCurrentWeekSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))")
    fun getCurrentMonthSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))")
    fun getCurrentYearSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) ")
    fun getRangeDateSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="", isDeleted: Boolean = true,startDate:Long, endDate:Long): Flow<List<SmsEntity>>



    @Query("SELECT * FROM SmsEntity WHERE body LIKE '%' || :query || '%'")
    fun getAllSms(query:String=""): PagingSource<Int,SmsEntity>


    @Query("DELETE FROM SmsEntity WHERE senderId = :senderId")
    suspend fun deleteSenderSms(senderId:Int)

}