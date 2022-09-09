package com.msharialsayari.musrofaty.data_layer.database.sms_database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msharialsayari.musrofaty.layer_data.database.sms_database.SmsEntity

@Dao
interface SmsDao {


    @Query("SELECT * FROM SmsEntity WHERE bankName COLLATE NOCASE IN (:banksNames) AND isDeleted = :isDeleted")
    suspend fun getAll(banksNames:List<String>,isDeleted:Boolean): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE bankName COLLATE NOCASE IN (:banksNames)")
    suspend fun getAllNoCheckIsDeleted(banksNames:List<String>): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE LOWER(bankName)  = LOWER(:bankName) AND isDeleted = :isDeleted")
    suspend fun getSmsByBankName(bankName:String,isDeleted:Boolean): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE LOWER(bankName)  = LOWER(:bankName)")
    suspend fun getSmsByBankNameNoCheckIsDeleted(bankName:String): List<SmsEntity>

    @Query("SELECT * FROM SmsEntity WHERE smsId = :smsID")
    suspend fun getSms(smsID:String): SmsEntity?

    @Query("UPDATE SmsEntity SET isDeleted = :isDeleted WHERE smsId IN (:list)")
    suspend fun setDeletedSms( list: List<String>, isDeleted: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg smsEntity: SmsEntity)

}