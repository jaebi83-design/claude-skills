package com.inventoryshopping.meijer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_aisle_mappings",
    foreignKeys = [
        ForeignKey(
            entity = StoreSectionEntity::class,
            parentColumns = ["sectionId"],
            childColumns = ["sectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("storeId"),
        Index("canonicalName"),
        Index("sectionId")
    ]
)
data class ProductAisleMappingEntity(
    @PrimaryKey(autoGenerate = true) val mappingId: Long = 0,
    val storeId: String,
    val canonicalName: String,
    val displayName: String,
    val sectionId: String,
    val category: String? = null,
    val confidence: String = "SEED", // SEED, USER_ASSIGNED, USER_VERIFIED
    val lastVerified: Long = System.currentTimeMillis()
)
