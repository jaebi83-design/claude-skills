package com.inventoryshopping.meijer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_section_defaults",
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
        Index("sectionId")
    ]
)
data class CategorySectionDefaultEntity(
    @PrimaryKey val categoryDefaultId: String,
    val storeId: String,
    val category: String,
    val sectionId: String,
    val keywords: String // comma-separated keywords
)
