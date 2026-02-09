package com.inventoryshopping.meijer.domain.model

data class Store(
    val storeId: String,
    val storeName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String
)
