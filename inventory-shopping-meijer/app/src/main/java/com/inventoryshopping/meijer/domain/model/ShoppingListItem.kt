package com.inventoryshopping.meijer.domain.model

data class ShoppingListItem(
    val itemId: String,
    val listId: String,
    val rawText: String,
    val itemName: String,
    val quantity: String? = null,
    val sectionId: String? = null,
    val isChecked: Boolean = false,
    val sortOrder: Int = 0,
    val addedAt: Long = System.currentTimeMillis()
)
