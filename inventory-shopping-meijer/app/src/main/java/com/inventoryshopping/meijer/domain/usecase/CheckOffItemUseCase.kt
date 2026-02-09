package com.inventoryshopping.meijer.domain.usecase

import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import javax.inject.Inject

class CheckOffItemUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    suspend operator fun invoke(itemId: String, isChecked: Boolean) {
        repository.updateCheckedStatus(itemId, isChecked)
    }
}
