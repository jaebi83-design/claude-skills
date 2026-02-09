package com.inventoryshopping.meijer.data.seed

import android.content.Context
import com.inventoryshopping.meijer.data.local.dao.CategorySectionDefaultDao
import com.inventoryshopping.meijer.data.local.dao.ProductAisleMappingDao
import com.inventoryshopping.meijer.data.local.dao.StoreDao
import com.inventoryshopping.meijer.data.local.dao.StoreSectionDao
import com.inventoryshopping.meijer.data.local.entity.CategorySectionDefaultEntity
import com.inventoryshopping.meijer.data.local.entity.ProductAisleMappingEntity
import com.inventoryshopping.meijer.data.local.entity.StoreEntity
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

@Serializable
data class SeedStore(
    val storeId: String,
    val storeName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val isDefault: Boolean
)

@Serializable
data class SeedSection(
    val sectionId: String,
    val sectionType: String,
    val sectionName: String,
    val sortOrder: Int,
    val description: String? = null
)

@Serializable
data class SeedCategoryDefault(
    val categoryDefaultId: String,
    val category: String,
    val sectionId: String,
    val keywords: String
)

@Serializable
data class SeedProductMapping(
    val canonicalName: String,
    val displayName: String,
    val sectionId: String,
    val category: String? = null
)

@Serializable
data class SeedData(
    val store: SeedStore,
    val sections: List<SeedSection>,
    val categoryDefaults: List<SeedCategoryDefault>,
    val productMappings: List<SeedProductMapping>
)

class SeedDataLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storeDao: StoreDao,
    private val sectionDao: StoreSectionDao,
    private val mappingDao: ProductAisleMappingDao,
    private val categoryDefaultDao: CategorySectionDefaultDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadSeedDataIfNeeded() {
        if (storeDao.getStoreCount() > 0) return

        val jsonString = context.assets
            .open("seed_data/meijer_store_default.json")
            .bufferedReader()
            .use { it.readText() }

        val seedData = json.decodeFromString<SeedData>(jsonString)

        // Insert store
        storeDao.insertStore(
            StoreEntity(
                storeId = seedData.store.storeId,
                storeName = seedData.store.storeName,
                address = seedData.store.address,
                city = seedData.store.city,
                state = seedData.store.state,
                zipCode = seedData.store.zipCode,
                isDefault = seedData.store.isDefault
            )
        )

        // Insert sections
        sectionDao.insertSections(
            seedData.sections.map { section ->
                StoreSectionEntity(
                    sectionId = section.sectionId,
                    storeId = seedData.store.storeId,
                    sectionType = section.sectionType,
                    sectionName = section.sectionName,
                    sortOrder = section.sortOrder,
                    description = section.description
                )
            }
        )

        // Insert category defaults
        categoryDefaultDao.insertDefaults(
            seedData.categoryDefaults.map { cat ->
                CategorySectionDefaultEntity(
                    categoryDefaultId = cat.categoryDefaultId,
                    storeId = seedData.store.storeId,
                    category = cat.category,
                    sectionId = cat.sectionId,
                    keywords = cat.keywords
                )
            }
        )

        // Insert product mappings
        mappingDao.insertMappings(
            seedData.productMappings.map { product ->
                ProductAisleMappingEntity(
                    storeId = seedData.store.storeId,
                    canonicalName = product.canonicalName,
                    displayName = product.displayName,
                    sectionId = product.sectionId,
                    category = product.category,
                    confidence = "SEED"
                )
            }
        )
    }
}
