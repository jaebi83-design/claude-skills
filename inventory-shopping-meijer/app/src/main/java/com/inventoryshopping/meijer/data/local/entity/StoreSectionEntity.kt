package com.inventoryshopping.meijer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "store_sections",
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
data class StoreSectionEntity(
    @PrimaryKey val sectionId: String,
    val storeId: String,
    val sectionType: String, // "PERIMETER_ZONE" or "CENTER_AISLE"
    val sectionName: String,
    val sortOrder: Int,
    val description: String? = null
)
