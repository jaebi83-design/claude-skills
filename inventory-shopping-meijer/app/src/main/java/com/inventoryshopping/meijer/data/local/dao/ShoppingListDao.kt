package com.inventoryshopping.meijer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists WHERE isActive = 1 ORDER BY createdAt DESC")
    fun observeActiveLists(): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE listId = :listId")
    suspend fun getListById(listId: String): ShoppingListEntity?

    @Query("SELECT * FROM shopping_lists WHERE listId = :listId")
    fun observeListById(listId: String): Flow<ShoppingListEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingListEntity)

    @Query("DELETE FROM shopping_lists WHERE listId = :listId")
    suspend fun deleteList(listId: String)
}
