package com.inventoryshopping.meijer.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ShoppingList : Screen("shopping_list/{listId}") {
        fun createRoute(listId: String) = "shopping_list/$listId"
    }
    data object Settings : Screen("settings")
}
