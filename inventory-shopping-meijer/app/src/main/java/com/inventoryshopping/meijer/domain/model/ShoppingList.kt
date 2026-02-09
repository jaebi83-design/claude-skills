package com.inventoryshopping.meijer.domain.model

enum class ListSource {
    MANUAL,
    SKYLIGHT,
    CLIPBOARD
}

data class ShoppingList(
    val listId: String,
    val name: String,
    val storeId: String,
    val createdAt: Long,
    val isActive: Boolean,
    val sourceType: ListSource
)
