package com.inventoryshopping.meijer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inventoryshopping.meijer.data.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Query("SELECT * FROM stores WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultStore(): StoreEntity?

    @Query("SELECT * FROM stores WHERE isDefault = 1 LIMIT 1")
    fun observeDefaultStore(): Flow<StoreEntity?>

    @Query("SELECT * FROM stores ORDER BY storeName")
    fun observeAllStores(): Flow<List<StoreEntity>>

    @Query("SELECT * FROM stores WHERE storeId = :storeId")
    suspend fun getStoreById(storeId: String): StoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreEntity)

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getStoreCount(): Int
}
