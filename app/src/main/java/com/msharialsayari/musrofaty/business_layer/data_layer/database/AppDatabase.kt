package com.msharialsayari.musrofaty.business_layer.data_layer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database.ContentDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database.ContentEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.layer_data.database.Convertors


@Database(
    entities = [SmsEntity::class,
        FilterEntity::class,
        FilterAdvancedEntity::class,
        CategoryEntity::class,
        StoreEntity::class,
        WordDetectorEntity::class,
        SenderEntity::class,
        ContentEntity::class, ],
    version = 19,
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
    abstract fun senderDao(): SenderDao
    abstract fun contentDao(): ContentDao

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


        database.execSQL("CREATE TABLE `SmsEntity`  (`id` TEXT PRIMARY KEY NOT NULL ,`senderName` TEXT NOT NULL DEFAULT('') ,`timestamp` INTEGER NOT NULL DEFAULT(0), `body` TEXT NOT NULL DEFAULT(''),`senderId` INTEGER NOT NULL DEFAULT(0))")
        database.execSQL("CREATE TABLE `FilterAdvancedEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL,`smsType` TEXT NOT NULL,`words` TEXT NOT NULL, `filterOption` TEXT NOT NULL, `dateFrom` INTEGER NOT NULL DEFAULT(0), `dateTo` INTEGER NOT NULL DEFAULT(0))")
        database.execSQL("CREATE TABLE `SenderEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `displayNameAr` TEXT ,`displayNameEn` TEXT,`isPined` INTEGER DEFAULT(0) NOT NULL,`isActive` INTEGER DEFAULT(1) NOT NULL,`contentId` INTEGER NOT NULL DEFAULT(0))")
        database.execSQL("CREATE TABLE `WordDetectorEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL,`type` TEXT NOT NULL,`isActive` INTEGER NOT NULL DEFAULT(1))")
        database.execSQL("CREATE TABLE `ContentEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`contentKey` TEXT NOT NULL ,`value_ar` TEXT ,`value_en` TEXT)")

    }
}

val MIGRATION_12_13= object : Migration(12,13) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `SenderEntity` ADD `icon` INTEGER")
    }
}

val MIGRATION_13_14= object : Migration(13,14) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `SmsEntity` " )
        database.execSQL("CREATE TABLE `SmsEntity`  (`id` TEXT PRIMARY KEY NOT NULL ,`senderName` TEXT NOT NULL DEFAULT('') ,`timestamp` INTEGER NOT NULL DEFAULT(0), `body` TEXT NOT NULL DEFAULT(''),`senderId` INTEGER NOT NULL DEFAULT(0))")
    }
}

val MIGRATION_14_15= object : Migration(14,15) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `SmsEntity` ADD `isFavorite` INTEGER NOT NULL DEFAULT(0)")
    }
}

val MIGRATION_15_16= object : Migration(15,16) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `SenderEntity`" )
        database.execSQL("CREATE TABLE `SenderEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `displayNameAr` TEXT ,`displayNameEn` TEXT,`isPined` INTEGER DEFAULT(0) NOT NULL,`isActive` INTEGER DEFAULT(1) NOT NULL,`contentId` INTEGER NOT NULL DEFAULT(0))")

    }
}


val MIGRATION_16_17= object : Migration(16,17) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `FilterAdvancedEntity`" )
        database.execSQL("CREATE TABLE `FilterAdvancedEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL,`words` TEXT NOT NULL,`senderId` INTEGER NOT NULL DEFAULT(0) )")

    }
}


val MIGRATION_17_18= object : Migration(17,18) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `WordDetectorEntity`" )
        database.execSQL("CREATE TABLE `WordDetectorEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL,`type` TEXT NOT NULL)")

    }
}

val MIGRATION_18_19= object : Migration(18,19) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `SenderEntity`" )
        database.execSQL("CREATE TABLE `SenderEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `senderName` TEXT   NOT NULL,`displayNameAr` TEXT  NOT NULL  ,`displayNameEn` TEXT  NOT NULL,`isPined` INTEGER DEFAULT(0) NOT NULL,`isActive` INTEGER DEFAULT(1) NOT NULL,`contentId` INTEGER  NOT NULL )")


    }
}




