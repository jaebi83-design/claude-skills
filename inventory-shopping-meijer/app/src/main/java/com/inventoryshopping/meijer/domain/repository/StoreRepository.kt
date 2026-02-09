package com.inventoryshopping.meijer.domain.repository

import com.inventoryshopping.meijer.data.local.entity.StoreEntity
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    suspend fun getDefaultStore(): StoreEntity?
    fun observeDefaultStore(): Flow<StoreEntity?>
    suspend fun getStoreById(storeId: String): StoreEntity?
    suspend fun insertStore(store: StoreEntity)
    suspend fun getStoreCount(): Int
    suspend fun getSectionsByStore(storeId: String): List<StoreSectionEntity>
    fun observeSectionsByStore(storeId: String): Flow<List<StoreSectionEntity>>
    suspend fun getSectionById(sectionId: String): StoreSectionEntity?
    suspend fun insertSections(sections: List<StoreSectionEntity>)
}
