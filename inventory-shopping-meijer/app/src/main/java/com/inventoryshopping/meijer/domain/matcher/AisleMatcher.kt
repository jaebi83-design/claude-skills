package com.inventoryshopping.meijer.domain.matcher

import com.inventoryshopping.meijer.domain.repository.AisleMappingRepository
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import javax.inject.Inject

data class AisleMatchResult(
    val sectionId: String,
    val sortOrder: Int
)

class AisleMatcher @Inject constructor(
    private val mappingRepository: AisleMappingRepository,
    private val storeRepository: StoreRepository,
    private val normalizer: ItemNameNormalizer,
    private val categoryInferrer: CategoryInferrer
) {
    suspend fun resolveAisle(itemName: String, storeId: String): AisleMatchResult? {
        val canonical = normalizer.normalize(itemName)

        // Tier 1: Exact match on canonical name in product_aisle_mappings
        val exactMatch = mappingRepository.findExactMapping(canonical, storeId)
        if (exactMatch != null) {
            val section = storeRepository.getSectionById(exactMatch.sectionId)
            if (section != null) {
                return AisleMatchResult(section.sectionId, section.sortOrder)
            }
        }

        // Tier 2: Partial match — check if canonical name contains a known product name
        val partialMatch = mappingRepository.findPartialMapping(canonical, storeId)
        if (partialMatch != null) {
            val section = storeRepository.getSectionById(partialMatch.sectionId)
            if (section != null) {
                return AisleMatchResult(section.sectionId, section.sortOrder)
            }
        }

        // Tier 3: Category inference via heuristic rules
        val inferredCategory = categoryInferrer.infer(canonical)
        if (inferredCategory != null) {
            val categoryDefault = mappingRepository.findCategoryDefault(inferredCategory, storeId)
            if (categoryDefault != null) {
                val section = storeRepository.getSectionById(categoryDefault.sectionId)
                if (section != null) {
                    return AisleMatchResult(section.sectionId, section.sortOrder)
                }
            }
        }

        // Tier 4: Keyword scan against category_section_defaults keywords
        val words = normalizer.extractWords(canonical)
        for (word in words) {
            val keywordMatch = mappingRepository.findByKeyword(word, storeId)
            if (keywordMatch != null) {
                val section = storeRepository.getSectionById(keywordMatch.sectionId)
                if (section != null) {
                    return AisleMatchResult(section.sectionId, section.sortOrder)
                }
            }
        }

        // No match found — item is unknown
        return null
    }
}
