package com.inventoryshopping.meijer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "shopping_lists",
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["storeId"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("storeId")]
)
data class ShoppingListEntity(
    @PrimaryKey val listId: String = UUID.randomUUID().toString(),
    val name: String,
    val storeId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val sourceType: String = "MANUAL" // MANUAL, SKYLIGHT, CLIPBOARD
)
