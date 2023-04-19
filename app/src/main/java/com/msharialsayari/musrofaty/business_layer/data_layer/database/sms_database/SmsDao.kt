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
    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' order by timestamp DESC ")
     fun getAllDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
     fun getTodayDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
     fun getCurrentWeekDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
     fun getCurrentMonthDashboard(query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
     fun getCurrentYearDashboard(query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE  body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC ")
     fun getRangeDateDashboard(query:String="", startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>



    ////////Statistics///////////

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' order by timestamp DESC ")
    suspend fun getAll(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
    suspend fun getToday(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
    suspend fun getCurrentWeek(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
    suspend fun getCurrentMonth(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
    suspend fun getCurrentYear(senderId:Int, query:String="", isDeleted: Boolean=false): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC  ")
    suspend fun getRangeDate(senderId:Int, query:String="", isDeleted: Boolean=false, startDate:Long, endDate:Long): List<SmsEntity>

    ////////Generating file and for sender sms list screen///////////

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' order by timestamp DESC ")
     fun getAllSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getTodaySms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getCurrentWeekSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
    fun getCurrentMonthSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
    fun getCurrentYearSms(senderId:Int, query:String="", isDeleted: Boolean=false): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC  ")
    fun getRangeDateSms(senderId:Int, query:String="", isDeleted: Boolean=false, startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>


    ///////Favorite/////////
    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getAllFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getTodayFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getCurrentWeekFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
    fun getCurrentMonthFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
    fun getCurrentYearFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isFavorite=:isFavorite AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC  ")
    fun getRangeDateFavoriteSms(senderId:Int, isFavorite: Boolean = true, query:String="", startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>


    //////Soft deleted////////
    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isFavorite AND body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getAllSoftDeletedSms(senderId:Int, isFavorite: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getTodaySoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getCurrentWeekSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
    fun getCurrentMonthSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
    fun getCurrentYearSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String=""): PagingSource<Int,SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC  ")
    fun getRangeDateSoftDeletedSms(senderId:Int, isDeleted: Boolean = true, query:String="", startDate:Long, endDate:Long): PagingSource<Int,SmsEntity>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getTodaySmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getCurrentWeekSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
    fun getCurrentMonthSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
    fun getCurrentYearSmsBySenderId(senderId:Int, query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC  ")
    fun getRangeDateSmsBySenderId(senderId:Int, query:String="", startDate:Long, endDate:Long): Flow<List<SmsEntity>>


    //////Check soft Delete//////
    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getTodaySmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime')) order by timestamp DESC ")
    fun getCurrentWeekSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now')) order by timestamp DESC ")
    fun getCurrentMonthSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>


    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now')) order by timestamp DESC ")
    fun getCurrentYearSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="",isDeleted: Boolean = true,): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE senderId =:senderId AND isDeleted=:isDeleted  AND body LIKE '%' || :query || '%' AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(:startDate/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(:endDate/1000,'unixepoch', 'localtime')) order by timestamp DESC  ")
    fun getRangeDateSmsBySenderIdWithSoftDeleteCheck(senderId:Int, query:String="", isDeleted: Boolean = true,startDate:Long, endDate:Long): Flow<List<SmsEntity>>



    @Query("SELECT * FROM SmsEntity WHERE body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getAllSms(query:String=""): PagingSource<Int,SmsEntity>


    @Query("DELETE FROM SmsEntity WHERE senderId = :senderId")
    suspend fun deleteSenderSms(senderId:Int)

}