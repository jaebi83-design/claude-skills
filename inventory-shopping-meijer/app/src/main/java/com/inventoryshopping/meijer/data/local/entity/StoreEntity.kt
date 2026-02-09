package com.inventoryshopping.meijer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey val storeId: String,
    val storeName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
