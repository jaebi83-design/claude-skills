package com.inventoryshopping.meijer.domain.repository

import com.inventoryshopping.meijer.data.local.entity.CategorySectionDefaultEntity
import com.inventoryshopping.meijer.data.local.entity.ProductAisleMappingEntity

interface AisleMappingRepository {
    suspend fun findExactMapping(canonicalName: String, storeId: String): ProductAisleMappingEntity?
    suspend fun findPartialMapping(canonicalName: String, storeId: String): ProductAisleMappingEntity?
    suspend fun findCategoryDefault(category: String, storeId: String): CategorySectionDefaultEntity?
    suspend fun findByKeyword(keyword: String, storeId: String): CategorySectionDefaultEntity?
    suspend fun getAllCategoryDefaults(storeId: String): List<CategorySectionDefaultEntity>
    suspend fun saveMapping(mapping: ProductAisleMappingEntity)
    suspend fun saveMappings(mappings: List<ProductAisleMappingEntity>)
    suspend fun saveCategoryDefaults(defaults: List<CategorySectionDefaultEntity>)
    suspend fun getMappingCount(storeId: String): Int
}
