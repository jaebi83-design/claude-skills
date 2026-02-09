package com.inventoryshopping.meijer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreSectionDao {

    @Query("SELECT * FROM store_sections WHERE storeId = :storeId ORDER BY sortOrder")
    fun observeSectionsByStore(storeId: String): Flow<List<StoreSectionEntity>>

    @Query("SELECT * FROM store_sections WHERE storeId = :storeId ORDER BY sortOrder")
    suspend fun getSectionsByStore(storeId: String): List<StoreSectionEntity>

    @Query("SELECT * FROM store_sections WHERE sectionId = :sectionId")
    suspend fun getSectionById(sectionId: String): StoreSectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: StoreSectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(sections: List<StoreSectionEntity>)
}
