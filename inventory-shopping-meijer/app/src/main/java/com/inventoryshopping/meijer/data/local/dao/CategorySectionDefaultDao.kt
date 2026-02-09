package com.inventoryshopping.meijer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventoryshopping.meijer.data.local.entity.CategorySectionDefaultEntity

@Dao
interface CategorySectionDefaultDao {

    @Query(
        "SELECT * FROM category_section_defaults " +
        "WHERE category = :category AND storeId = :storeId LIMIT 1"
    )
    suspend fun findByCategory(category: String, storeId: String): CategorySectionDefaultEntity?

    @Query(
        "SELECT * FROM category_section_defaults " +
        "WHERE storeId = :storeId AND (',' || keywords || ',') LIKE '%,' || :keyword || ',%' LIMIT 1"
    )
    suspend fun findByKeyword(keyword: String, storeId: String): CategorySectionDefaultEntity?

    @Query("SELECT * FROM category_section_defaults WHERE storeId = :storeId")
    suspend fun getAllForStore(storeId: String): List<CategorySectionDefaultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefaults(defaults: List<CategorySectionDefaultEntity>)
}
