package com.inventoryshopping.meijer.data.repository

import com.inventoryshopping.meijer.data.local.dao.ShoppingListDao
import com.inventoryshopping.meijer.data.local.dao.ShoppingListItemDao
import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity
import com.inventoryshopping.meijer.data.local.entity.ShoppingListItemEntity
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val listDao: ShoppingListDao,
    private val itemDao: ShoppingListItemDao
) : ShoppingListRepository {

    override fun observeActiveLists(): Flow<List<ShoppingListEntity>> =
        listDao.observeActiveLists()

    override fun observeListById(listId: String): Flow<ShoppingListEntity?> =
        listDao.observeListById(listId)

    override suspend fun getListById(listId: String): ShoppingListEntity? =
        listDao.getListById(listId)

    override suspend fun createList(list: ShoppingListEntity) =
        listDao.insertList(list)

    override suspend fun deleteList(listId: String) =
        listDao.deleteList(listId)

    override fun observeItemsByList(listId: String): Flow<List<ShoppingListItemEntity>> =
        itemDao.observeItemsByList(listId)

    override suspend fun getItemsByList(listId: String): List<ShoppingListItemEntity> =
        itemDao.getItemsByList(listId)

    override suspend fun addItem(item: ShoppingListItemEntity) =
        itemDao.insertItem(item)

    override suspend fun addItems(items: List<ShoppingListItemEntity>) =
        itemDao.insertItems(items)

    override suspend fun updateCheckedStatus(itemId: String, isChecked: Boolean) =
        itemDao.updateCheckedStatus(itemId, isChecked)

    override suspend fun updateItemSection(itemId: String, sectionId: String, sortOrder: Int) =
        itemDao.updateItemSection(itemId, sectionId, sortOrder)

    override suspend fun deleteItem(itemId: String) =
        itemDao.deleteItem(itemId)

    override fun observeUnknownItemCount(listId: String): Flow<Int> =
        itemDao.observeUnknownItemCount(listId)
}
