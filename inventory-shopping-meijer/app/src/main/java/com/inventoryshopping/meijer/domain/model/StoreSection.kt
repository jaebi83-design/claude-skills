package com.inventoryshopping.meijer.domain.model

enum class SectionType {
    PERIMETER_ZONE,
    CENTER_AISLE
}

data class StoreSection(
    val sectionId: String,
    val storeId: String,
    val sectionType: SectionType,
    val sectionName: String,
    val sortOrder: Int,
    val description: String? = null
)
