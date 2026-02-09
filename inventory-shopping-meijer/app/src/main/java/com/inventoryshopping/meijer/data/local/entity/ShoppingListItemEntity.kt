package com.inventoryshopping.meijer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "shopping_list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["listId"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StoreSectionEntity::class,
            parentColumns = ["sectionId"],
            childColumns = ["sectionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("listId"),
        Index("sectionId")
    ]
)
data class ShoppingListItemEntity(
    @PrimaryKey val itemId: String = UUID.randomUUID().toString(),
    val listId: String,
    val rawText: String,
    val itemName: String,
    val quantity: String? = null,
    val sectionId: String? = null,
    val isChecked: Boolean = false,
    val sortOrder: Int = 0,
    val addedAt: Long = System.currentTimeMillis()
)
