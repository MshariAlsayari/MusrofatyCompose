package com.msharialsayari.musrofaty.business_layer.data_layer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.layer_data.database.Convertors


@Database(
    entities = [SmsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertors::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smmDao(): SmsDao


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

