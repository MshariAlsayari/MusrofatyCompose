package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database


import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow


@Dao
interface SmsDao {

    @Query("SELECT * FROM SmsEntity WHERE id = :id")
    suspend fun getSms(id:String): SmsEntity?

    @Query("SELECT * FROM SmsEntity WHERE (LOWER(body) LiKE '%at:%' OR body LiKE '%لدى:%' OR body LiKE '%من:%' OR body LiKE '%في:%') AND (LOWER(body) LIKE LOWER(:storeName) || '%' OR LOWER(body) LIKE '%' ||  LOWER(:storeName) )  ")
    suspend fun getAllSmsContainsStore(storeName: String): List<SmsEntity>

    @Query("UPDATE SmsEntity SET isFavorite =:favorite WHERE id=:id")
    suspend fun favoriteSms(id:String, favorite:Boolean)

    @Query("UPDATE SmsEntity SET isDeleted =:delete WHERE id=:id")
    suspend fun softDelete(id:String, delete:Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg smsEntity: SmsEntity)

    @RawQuery(observedEntities = [SmsEntity::class])
    suspend fun getAllSms(query: SupportSQLiteQuery): List<SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
     fun getPaginationAllSms(query: SupportSQLiteQuery): PagingSource<Int,SmsEntity>

    @RawQuery(observedEntities = [SmsEntity::class])
    fun observingSmsList(query: SupportSQLiteQuery): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE id IN (:ids)")
    fun observingSmsListByIds(ids: List<String>): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun observingSmsListByQuery(query:String=""): Flow<List<SmsEntity>>

    @Query("SELECT * FROM SmsEntity WHERE body LIKE '%' || :query || '%' order by timestamp DESC ")
    fun getPaginationAllSms(query:String=""): PagingSource<Int,SmsEntity>

    @Query("DELETE FROM SmsEntity WHERE senderId = :senderId")
    suspend fun deleteSenderSms(senderId:Int)

}