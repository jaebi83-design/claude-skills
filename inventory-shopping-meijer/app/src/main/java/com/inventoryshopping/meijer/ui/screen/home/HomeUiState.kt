package com.inventoryshopping.meijer.ui.screen.home

import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity

data class HomeUiState(
    val lists: List<ShoppingListEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showNewListDialog: Boolean = false,
    val defaultStoreId: String? = null
)
