package com.inventoryshopping.meijer.domain.usecase

import com.inventoryshopping.meijer.data.local.entity.ShoppingListItemEntity
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class AisleGroupResult(
    val section: StoreSectionEntity,
    val items: List<ShoppingListItemEntity>
)

data class SortedListResult(
    val aisleGroups: List<AisleGroupResult>,
    val unknownItems: List<ShoppingListItemEntity>
)

class SortListByAisleUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val storeRepository: StoreRepository
) {
    fun observe(listId: String, storeId: String): Flow<SortedListResult> {
        val itemsFlow = shoppingListRepository.observeItemsByList(listId)
        val sectionsFlow = storeRepository.observeSectionsByStore(storeId)

        return combine(itemsFlow, sectionsFlow) { items, sections ->
            buildSortedResult(items, sections)
        }
    }

    private fun buildSortedResult(
        items: List<ShoppingListItemEntity>,
        sections: List<StoreSectionEntity>
    ): SortedListResult {
        val sectionMap = sections.associateBy { it.sectionId }
        val unknownItems = mutableListOf<ShoppingListItemEntity>()
        val groupedItems = mutableMapOf<String, MutableList<ShoppingListItemEntity>>()

        for (item in items) {
            val sectionId = item.sectionId
            if (sectionId == null || !sectionMap.containsKey(sectionId)) {
                unknownItems.add(item)
            } else {
                groupedItems.getOrPut(sectionId) { mutableListOf() }.add(item)
            }
        }

        // Build aisle groups sorted by walk-order (sortOrder)
        val aisleGroups = groupedItems.entries
            .mapNotNull { (sectionId, sectionItems) ->
                sectionMap[sectionId]?.let { section ->
                    AisleGroupResult(section = section, items = sectionItems)
                }
            }
            .sortedBy { it.section.sortOrder }

        return SortedListResult(
            aisleGroups = aisleGroups,
            unknownItems = unknownItems
        )
    }
}
