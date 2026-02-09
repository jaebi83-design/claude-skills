package com.inventoryshopping.meijer.domain.usecase

import com.inventoryshopping.meijer.data.local.entity.ShoppingListItemEntity
import com.inventoryshopping.meijer.domain.matcher.AisleMatcher
import com.inventoryshopping.meijer.domain.matcher.QuantityParser
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import javax.inject.Inject

class AddItemToListUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
    private val quantityParser: QuantityParser,
    private val aisleMatcher: AisleMatcher
) {
    suspend operator fun invoke(rawText: String, listId: String, storeId: String) {
        val parsed = quantityParser.parse(rawText)
        val matchResult = aisleMatcher.resolveAisle(parsed.itemName, storeId)

        val item = ShoppingListItemEntity(
            listId = listId,
            rawText = rawText,
            itemName = parsed.itemName,
            quantity = parsed.quantity,
            sectionId = matchResult?.sectionId,
            sortOrder = matchResult?.sortOrder ?: Int.MAX_VALUE
        )

        repository.addItem(item)
    }
}
