package com.msharialsayari.musrofaty.business_layer.data_layer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.layer_data.database.Convertors
import com.msharialsayari.musrofaty.layer_data.database.filter_database.FilterDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity


@Database(
    entities = [SmsEntity::class, FilterEntity::class, FilterAdvancedEntity::class, CategoryEntity::class, StoreEntity::class, WordDetectorEntity::class],
    version = 12,
    exportSchema = false
)
@TypeConverters(Convertors::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smmDao(): SmsDao
    abstract fun filterDao(): FilterDao
    abstract fun filterAdvancedDao(): FilterAdvancedDao
    abstract fun categoryDto(): CategoryDao
    abstract fun storeDao(): StoreDao
    abstract fun wordDto(): WordDetectorDao


}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `WalletEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL,`type` TEXT NOT NULL,`banks` TEXT NOT NULL, `annualCreditLimit` REAL NOT NULL, `createdDate` INTEGER NOT NULL)")
    }
}

val FILTER_MIGRATION_2_1 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `FilterEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL,`smsType` TEXT NOT NULL,`searchWord` TEXT NOT NULL, `date` TEXT NOT NULL)")
    }
}

val FILTER_MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `FilterEntity` ADD `bankName` TEXT")
    }
}

val SMS_CATEGORY_MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `SmsEntity` ADD `smsCategory` INTEGER")
    }
}

val SMS_DELETE_FLAG_MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `SmsEntity` ADD `isDeleted` INTEGER DEFAULT(0)")
    }
}

val FILTER_DATE_FROM_MIGRATION_6_7 = object : Migration(6, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `FilterEntity` ADD `dateFrom` TEXT")
        database.execSQL("ALTER TABLE `FilterEntity` ADD `dateTo` TEXT")
    }
}


val MIGRATION_9_10= object : Migration(9,10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `CategoryEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `value_ar` TEXT ,`value_en` TEXT,`isDefault` INTEGER DEFAULT(0) NOT NULL)")
        database.execSQL("CREATE TABLE `StoreEntity`  (`storeName` TEXT PRIMARY KEY NOT NULL ,`categoryId` INTEGER NOT NULL)")
    }
}


val MIGRATION_10_11= object : Migration(10,11) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `SmsEntity` " )
        database.execSQL("CREATE TABLE `SmsEntity`  (`smsId` TEXT PRIMARY KEY NOT NULL ,`bankName` TEXT ,`smsDateTime` INTEGER NOT NULL, `smsBody` TEXT, `isDeleted` INTEGER DEFAULT(0) )")
    }
}

val MIGRATION_11_12= object : Migration(11,12) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `WalletEntity` " )
        database.execSQL("DROP TABLE `SmsEntity` " )


        database.execSQL("CREATE TABLE `SmsEntity`  (`id` TEXT PRIMARY KEY NOT NULL ,`senderName` TEXT NOT NULL DEFAULT('') ,`timestamp` INTEGER NOT NULL DEFAULT(0), `body` TEXT NOT NULL DEFAULT(''))")
        database.execSQL("CREATE TABLE `FilterAdvancedEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL,`smsType` TEXT NOT NULL,`words` TEXT NOT NULL, `filterOption` TEXT NOT NULL, `dateFrom` INTEGER NOT NULL DEFAULT(0), `dateTo` INTEGER NOT NULL DEFAULT(0))")
        database.execSQL("CREATE TABLE `WordDetectorEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL,`type` TEXT NOT NULL,`isActive` INTEGER NOT NULL DEFAULT(1))")


    }
}



