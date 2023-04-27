package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database


import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
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


    @RawQuery(observedEntities = [SmsEntity::class])
     fun getAllSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getTodaySms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentWeekSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentMonthSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>


    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentYearSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getRangeDateSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>


    ///////Favorite/////////
    @RawQuery(observedEntities = [SmsEntity::class])
    fun getAllFavoriteSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>


    @RawQuery(observedEntities = [SmsEntity::class])
    fun getTodayFavoriteSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentWeekFavoriteSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentMonthFavoriteSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>


    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentYearFavoriteSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getRangeDateFavoriteSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>


    //////Soft deleted////////
    @RawQuery(observedEntities = [SmsEntity::class])
    fun getAllSoftDeletedSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>


    @RawQuery(observedEntities = [SmsEntity::class])
    fun getTodaySoftDeletedSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentWeekSoftDeletedSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentMonthSoftDeletedSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentYearSoftDeletedSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getRangeDateSoftDeletedSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>



    //////Check soft Delete//////
    @RawQuery(observedEntities = [SmsEntity::class])
    fun getSmsBySenderIdWithSoftDeleteCheck(query: SupportSQLiteQuery): Flow<List<SmsEntity>>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getTodaySmsBySenderIdWithSoftDeleteCheck(query: SupportSQLiteQuery): Flow<List<SmsEntity>>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentWeekSmsBySenderIdWithSoftDeleteCheck(query: SupportSQLiteQuery): Flow<List<SmsEntity>>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentMonthSmsBySenderIdWithSoftDeleteCheck(query: SupportSQLiteQuery): Flow<List<SmsEntity>>


    @RawQuery(observedEntities = [SmsEntity::class])
    fun getCurrentYearSmsBySenderIdWithSoftDeleteCheck(query: SupportSQLiteQuery): Flow<List<SmsEntity>>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun getRangeDateSmsBySenderIdWithSoftDeleteCheck(query: SupportSQLiteQuery): Flow<List<SmsEntity>>



    @Query("SELECT * FROM SmsEntity WHERE body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getAllSms(query:String=""): PagingSource<Int,SmsEntity>


    @Query("DELETE FROM SmsEntity WHERE senderId = :senderId")
    suspend fun deleteSenderSms(senderId:Int)

}