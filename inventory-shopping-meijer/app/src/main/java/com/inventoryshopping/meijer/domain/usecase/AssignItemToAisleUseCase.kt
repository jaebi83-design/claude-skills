package com.inventoryshopping.meijer.domain.usecase

import com.inventoryshopping.meijer.data.local.entity.ProductAisleMappingEntity
import com.inventoryshopping.meijer.domain.matcher.ItemNameNormalizer
import com.inventoryshopping.meijer.domain.repository.AisleMappingRepository
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import javax.inject.Inject

class AssignItemToAisleUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val aisleMappingRepository: AisleMappingRepository,
    private val storeRepository: StoreRepository,
    private val normalizer: ItemNameNormalizer
) {
    suspend operator fun invoke(itemId: String, sectionId: String, storeId: String) {
        val section = storeRepository.getSectionById(sectionId) ?: return
        val item = shoppingListRepository.getItemsByList("").find { it.itemId == itemId }
            ?: return

        // Update the item's section and sort order on the shopping list
        shoppingListRepository.updateItemSection(itemId, sectionId, section.sortOrder)

        // Save the mapping so this item is automatically resolved next time
        val canonical = normalizer.normalize(item.itemName)
        val mapping = ProductAisleMappingEntity(
            storeId = storeId,
            canonicalName = canonical,
            displayName = item.itemName,
            sectionId = sectionId,
            confidence = "USER_ASSIGNED"
        )
        aisleMappingRepository.saveMapping(mapping)
    }
}
