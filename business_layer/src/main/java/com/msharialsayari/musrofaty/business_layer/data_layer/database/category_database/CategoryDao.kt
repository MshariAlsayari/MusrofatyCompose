package com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database

import androidx.room.*


@Dao
interface CategoryDao {


    @Query("SELECT * FROM CategoryEntity")
    suspend fun getAll(): List<CategoryEntity>


    @Transaction
    @Query("SELECT * FROM CategoryEntity")
    suspend fun getAllCategoriesWithStores(): List<CategoryWithStore>

    @Query("SELECT * FROM CategoryEntity WHERE isDefault = :isDefault")
    suspend fun getAllDefault(isDefault: Boolean): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE id = :id")
    suspend fun getCategory(id: Int): CategoryEntity?

    @Transaction
    @Query("SELECT * FROM CategoryEntity WHERE id = :id")
    suspend fun getCategoryAndStores(id: Int): CategoryWithStore?


    @Query("UPDATE CategoryEntity SET value_ar = :valueAr WHERE id = :id")
    suspend fun changeArabicCategory(id: Int, valueAr: String)

    @Query("UPDATE CategoryEntity SET value_en = :valueEn WHERE id = :id")
    suspend fun changeEnglishCategory(id: Int, valueEn: String)


    @Query("DELETE FROM CategoryEntity  WHERE id = :id")
    suspend fun deleteCategory(id: Int)

    @Delete
    suspend fun delete(vararg categoryEntity: CategoryEntity)

    @Query("DELETE FROM CategoryEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg categoryEntity: CategoryEntity)
}