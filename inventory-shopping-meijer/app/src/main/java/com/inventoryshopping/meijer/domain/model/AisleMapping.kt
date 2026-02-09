package com.inventoryshopping.meijer.domain.model

enum class MappingConfidence {
    SEED,
    USER_ASSIGNED,
    USER_VERIFIED
}

data class AisleMapping(
    val mappingId: Long = 0,
    val storeId: String,
    val canonicalName: String,
    val displayName: String,
    val sectionId: String,
    val category: String? = null,
    val confidence: MappingConfidence = MappingConfidence.SEED
)
