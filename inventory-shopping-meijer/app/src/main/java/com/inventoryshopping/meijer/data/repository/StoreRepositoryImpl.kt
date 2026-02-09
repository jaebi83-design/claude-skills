package com.inventoryshopping.meijer.data.repository

import com.inventoryshopping.meijer.data.local.dao.StoreDao
import com.inventoryshopping.meijer.data.local.dao.StoreSectionDao
import com.inventoryshopping.meijer.data.local.entity.StoreEntity
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val storeDao: StoreDao,
    private val sectionDao: StoreSectionDao
) : StoreRepository {

    override suspend fun getDefaultStore(): StoreEntity? =
        storeDao.getDefaultStore()

    override fun observeDefaultStore(): Flow<StoreEntity?> =
        storeDao.observeDefaultStore()

    override suspend fun getStoreById(storeId: String): StoreEntity? =
        storeDao.getStoreById(storeId)

    override suspend fun insertStore(store: StoreEntity) =
        storeDao.insertStore(store)

    override suspend fun getStoreCount(): Int =
        storeDao.getStoreCount()

    override suspend fun getSectionsByStore(storeId: String): List<StoreSectionEntity> =
        sectionDao.getSectionsByStore(storeId)

    override fun observeSectionsByStore(storeId: String): Flow<List<StoreSectionEntity>> =
        sectionDao.observeSectionsByStore(storeId)

    override suspend fun getSectionById(sectionId: String): StoreSectionEntity? =
        sectionDao.getSectionById(sectionId)

    override suspend fun insertSections(sections: List<StoreSectionEntity>) =
        sectionDao.insertSections(sections)
}
