package com.inventoryshopping.meijer.ui.screen.shoppinglist

import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity
import com.inventoryshopping.meijer.domain.usecase.SortedListResult

data class ShoppingListUiState(
    val list: ShoppingListEntity? = null,
    val sortedResult: SortedListResult? = null,
    val isLoading: Boolean = true,
    val newItemText: String = "",
    val showAssignAisleSheet: Boolean = false,
    val selectedItemId: String? = null,
    val selectedItemName: String? = null,
    val availableSections: List<StoreSectionEntity> = emptyList(),
    val storeId: String? = null
)
