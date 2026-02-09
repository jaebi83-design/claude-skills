package com.inventoryshopping.meijer.data.repository

import com.inventoryshopping.meijer.data.local.dao.CategorySectionDefaultDao
import com.inventoryshopping.meijer.data.local.dao.ProductAisleMappingDao
import com.inventoryshopping.meijer.data.local.entity.CategorySectionDefaultEntity
import com.inventoryshopping.meijer.data.local.entity.ProductAisleMappingEntity
import com.inventoryshopping.meijer.domain.repository.AisleMappingRepository
import javax.inject.Inject

class AisleMappingRepositoryImpl @Inject constructor(
    private val mappingDao: ProductAisleMappingDao,
    private val categoryDefaultDao: CategorySectionDefaultDao
) : AisleMappingRepository {

    override suspend fun findExactMapping(
        canonicalName: String,
        storeId: String
    ): ProductAisleMappingEntity? =
        mappingDao.findByCanonicalName(canonicalName, storeId)

    override suspend fun findPartialMapping(
        canonicalName: String,
        storeId: String
    ): ProductAisleMappingEntity? =
        mappingDao.findByPartialMatch(canonicalName, storeId)

    override suspend fun findCategoryDefault(
        category: String,
        storeId: String
    ): CategorySectionDefaultEntity? =
        categoryDefaultDao.findByCategory(category, storeId)

    override suspend fun findByKeyword(
        keyword: String,
        storeId: String
    ): CategorySectionDefaultEntity? =
        categoryDefaultDao.findByKeyword(keyword, storeId)

    override suspend fun getAllCategoryDefaults(
        storeId: String
    ): List<CategorySectionDefaultEntity> =
        categoryDefaultDao.getAllForStore(storeId)

    override suspend fun saveMapping(mapping: ProductAisleMappingEntity) =
        mappingDao.insertMapping(mapping)

    override suspend fun saveMappings(mappings: List<ProductAisleMappingEntity>) =
        mappingDao.insertMappings(mappings)

    override suspend fun saveCategoryDefaults(defaults: List<CategorySectionDefaultEntity>) =
        categoryDefaultDao.insertDefaults(defaults)

    override suspend fun getMappingCount(storeId: String): Int =
        mappingDao.getMappingCount(storeId)
}
