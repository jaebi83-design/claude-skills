package com.inventoryshopping.meijer.domain.repository

import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity
import com.inventoryshopping.meijer.data.local.entity.ShoppingListItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeActiveLists(): Flow<List<ShoppingListEntity>>
    fun observeListById(listId: String): Flow<ShoppingListEntity?>
    suspend fun getListById(listId: String): ShoppingListEntity?
    suspend fun createList(list: ShoppingListEntity)
    suspend fun deleteList(listId: String)

    fun observeItemsByList(listId: String): Flow<List<ShoppingListItemEntity>>
    suspend fun getItemsByList(listId: String): List<ShoppingListItemEntity>
    suspend fun addItem(item: ShoppingListItemEntity)
    suspend fun addItems(items: List<ShoppingListItemEntity>)
    suspend fun updateCheckedStatus(itemId: String, isChecked: Boolean)
    suspend fun updateItemSection(itemId: String, sectionId: String, sortOrder: Int)
    suspend fun deleteItem(itemId: String)
    fun observeUnknownItemCount(listId: String): Flow<Int>
}
