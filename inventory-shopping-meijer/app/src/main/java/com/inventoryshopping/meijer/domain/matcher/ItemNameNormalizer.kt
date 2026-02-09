package com.inventoryshopping.meijer.domain.matcher

import javax.inject.Inject

class ItemNameNormalizer @Inject constructor() {

    fun normalize(raw: String): String {
        return raw
            .lowercase()
            .trim()
            .replace(Regex("\\s+"), " ")
            .replace(Regex("^(a |an |the |some )"), "")
            .replace(Regex("^\\d+\\s*(oz|lb|lbs|ct|pk|pack|gallon|gal|qt|pt|ml|l|kg|g)\\s*"), "")
            .trim()
    }

    fun extractWords(normalized: String): List<String> {
        return normalized.split(" ").filter { it.isNotBlank() }
    }
}
