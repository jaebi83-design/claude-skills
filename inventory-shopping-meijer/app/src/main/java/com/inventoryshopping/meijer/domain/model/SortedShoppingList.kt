package com.inventoryshopping.meijer.domain.model

data class AisleGroup(
    val section: StoreSection?,
    val items: List<ShoppingListItem>
)

data class SortedShoppingList(
    val listId: String,
    val name: String,
    val aisleGroups: List<AisleGroup>,
    val unknownItems: List<ShoppingListItem>
)
