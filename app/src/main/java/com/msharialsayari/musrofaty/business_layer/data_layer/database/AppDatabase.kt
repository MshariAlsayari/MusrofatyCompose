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
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAmountEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterWordEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.layer_data.database.Convertors


@Database(
    entities = [SmsEntity::class,
                FilterEntity::class,
                FilterAdvancedEntity::class,
                FilterWordEntity::class,
                FilterAmountEntity::class,
                CategoryEntity::class,
                StoreEntity::class,
                WordDetectorEntity::class,
                SenderEntity::class,
                ContentEntity::class,
                StoreFirebaseEntity::class,],
    version = 29,
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
    abstract fun storeFirebaseDao(): StoreFirebaseDao

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

val MIGRATION_19_20= object : Migration(19,20) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `CategoryEntity`" )
        database.execSQL("DROP TABLE `StoreEntity`" )
        database.execSQL("CREATE TABLE `CategoryEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`sortOrder` INTEGER NOT NULL, `valueAr` TEXT ,`valueEn` TEXT)")
        database.execSQL("CREATE TABLE `StoreEntity`  (`name` TEXT PRIMARY KEY NOT NULL ,`categoryId` INTEGER NOT NULL, FOREIGN KEY(`categoryId`) REFERENCES `Entity`(`name`) ON UPDATE NO ACTION ON DELETE CASCADE )")
    }
}

val MIGRATION_20_21= object : Migration(20,21) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `CategoryEntity`" )
        database.execSQL("DROP TABLE `StoreEntity`" )
        database.execSQL("CREATE TABLE `CategoryEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`sortOrder` INTEGER NOT NULL, `valueAr` TEXT ,`valueEn` TEXT)")
        database.execSQL("CREATE TABLE `StoreEntity`  (`name` TEXT PRIMARY KEY NOT NULL ,`categoryId` INTEGER NOT NULL )")
    }
}


val MIGRATION_21_22= object : Migration(21,22) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `CategoryEntity`" )
        database.execSQL("DROP TABLE `StoreEntity`" )
        database.execSQL("CREATE TABLE `CategoryEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`sortOrder` INTEGER NOT NULL, `valueAr` TEXT ,`valueEn` TEXT)")
        database.execSQL("CREATE TABLE `StoreEntity`  (`name` TEXT PRIMARY KEY NOT NULL ,`category_id` INTEGER NOT NULL )")
    }
}


val MIGRATION_22_23= object : Migration(22,23) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `CategoryEntity`  ADD `isDefault` INTEGER NOT NULL DEFAULT(0)")

    }
}

val MIGRATION_23_24= object : Migration(23,24) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `StoreEntity`" )
        database.execSQL("CREATE TABLE `StoreEntity`  (`name` TEXT PRIMARY KEY NOT NULL ,`category_id` INTEGER NOT NULL )")
        database.execSQL("CREATE TABLE `StoreFirebaseEntity`  (`name` TEXT PRIMARY KEY NOT NULL ,`category_id` INTEGER NOT NULL )")
    }
}


val MIGRATION_24_25= object : Migration(24,25) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `SmsEntity` ADD `isDeleted` INTEGER NOT NULL DEFAULT(0)")
    }
}


val MIGRATION_25_26= object : Migration(25,26) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `SenderEntity`" )
        database.execSQL("DROP TABLE `SmsEntity`" )
        database.execSQL("CREATE TABLE `SenderEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `senderName` TEXT   NOT NULL,`displayNameAr` TEXT  NOT NULL  ,`displayNameEn` TEXT  NOT NULL,`isPined` INTEGER DEFAULT(0) NOT NULL,`contentId` INTEGER  NOT NULL )")
        database.execSQL("CREATE TABLE `SmsEntity`  (`id` TEXT PRIMARY KEY NOT NULL ,`senderName` TEXT NOT NULL DEFAULT('') ,`timestamp` INTEGER NOT NULL DEFAULT(0), `body` TEXT NOT NULL DEFAULT(''),`senderId` INTEGER NOT NULL DEFAULT(0),`isDeleted` INTEGER NOT NULL DEFAULT(0),`isFavorite` INTEGER NOT NULL DEFAULT(0))")
    }
}

val MIGRATION_26_27= object : Migration(26,27) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `SenderEntity` ADD `senderIconUri` TEXT   NOT NULL DEFAULT('')")
    }
}

val MIGRATION_27_28= object : Migration(27,28) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `FilterEntity`" )
        database.execSQL("CREATE TABLE `FilterEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL,`senderId` INTEGER NOT NULL DEFAULT(0) )")
        database.execSQL("CREATE TABLE `FilterWordEntity` (`wordId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`filterId` INTEGER NOT NULL DEFAULT(0), `word` TEXT NOT NULL,`logicOperator` TEXT NOT NULL DEFAULT('AND') )")
    }
}

val MIGRATION_28_29= object : Migration(28,29) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `FilterAmountEntity` (`amountId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`filterId` INTEGER NOT NULL DEFAULT(0), `amount` TEXT NOT NULL,`amountOperator` TEXT NOT NULL DEFAULT('EQUAL_OR_MORE') )")
    }
}




