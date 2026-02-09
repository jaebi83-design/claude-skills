package com.inventoryshopping.meijer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventoryshopping.meijer.data.local.entity.ShoppingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListItemDao {

    @Query("SELECT * FROM shopping_list_items WHERE listId = :listId ORDER BY sortOrder, addedAt")
    fun observeItemsByList(listId: String): Flow<List<ShoppingListItemEntity>>

    @Query("SELECT * FROM shopping_list_items WHERE listId = :listId ORDER BY sortOrder, addedAt")
    suspend fun getItemsByList(listId: String): List<ShoppingListItemEntity>

    @Query("SELECT * FROM shopping_list_items WHERE itemId = :itemId")
    suspend fun getItemById(itemId: String): ShoppingListItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingListItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingListItemEntity>)

    @Query("UPDATE shopping_list_items SET isChecked = :isChecked WHERE itemId = :itemId")
    suspend fun updateCheckedStatus(itemId: String, isChecked: Boolean)

    @Query(
        "UPDATE shopping_list_items SET sectionId = :sectionId, sortOrder = :sortOrder WHERE itemId = :itemId"
    )
    suspend fun updateItemSection(itemId: String, sectionId: String, sortOrder: Int)

    @Query("DELETE FROM shopping_list_items WHERE itemId = :itemId")
    suspend fun deleteItem(itemId: String)

    @Query("SELECT COUNT(*) FROM shopping_list_items WHERE listId = :listId AND sectionId IS NULL")
    fun observeUnknownItemCount(listId: String): Flow<Int>
}
