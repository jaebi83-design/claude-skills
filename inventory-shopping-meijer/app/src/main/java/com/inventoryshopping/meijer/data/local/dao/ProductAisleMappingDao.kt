package com.inventoryshopping.meijer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventoryshopping.meijer.data.local.entity.ProductAisleMappingEntity

@Dao
interface ProductAisleMappingDao {

    @Query(
        "SELECT * FROM product_aisle_mappings " +
        "WHERE canonicalName = :canonicalName AND storeId = :storeId LIMIT 1"
    )
    suspend fun findByCanonicalName(canonicalName: String, storeId: String): ProductAisleMappingEntity?

    @Query(
        "SELECT * FROM product_aisle_mappings " +
        "WHERE storeId = :storeId AND :canonicalName LIKE '%' || canonicalName || '%' LIMIT 1"
    )
    suspend fun findByPartialMatch(canonicalName: String, storeId: String): ProductAisleMappingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapping(mapping: ProductAisleMappingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMappings(mappings: List<ProductAisleMappingEntity>)

    @Query("SELECT COUNT(*) FROM product_aisle_mappings WHERE storeId = :storeId")
    suspend fun getMappingCount(storeId: String): Int
}
