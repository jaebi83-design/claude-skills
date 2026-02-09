package com.inventoryshopping.meijer.domain.matcher

import javax.inject.Inject

data class ParsedItem(
    val itemName: String,
    val quantity: String?
)

class QuantityParser @Inject constructor() {

    private val quantityPattern = Regex(
        "^(\\d+\\.?\\d*\\s*(?:oz|lb|lbs|ct|pk|pack|gallon|gal|qt|pt|ml|l|kg|g|dozen|doz|bunch|can|cans|box|boxes|bag|bags|bottle|bottles|jar|jars|roll|rolls|each)?\\s*)(.+)",
        RegexOption.IGNORE_CASE
    )

    private val countPattern = Regex(
        "^(\\d+)\\s+(.+)",
        RegexOption.IGNORE_CASE
    )

    fun parse(rawText: String): ParsedItem {
        val trimmed = rawText.trim()

        // Try quantity with unit first: "2 lbs ground beef"
        quantityPattern.find(trimmed)?.let { match ->
            val quantity = match.groupValues[1].trim()
            val itemName = match.groupValues[2].trim()
            if (itemName.isNotBlank()) {
                return ParsedItem(itemName = itemName, quantity = quantity)
            }
        }

        // Try simple count: "3 apples"
        countPattern.find(trimmed)?.let { match ->
            val quantity = match.groupValues[1].trim()
            val itemName = match.groupValues[2].trim()
            if (itemName.isNotBlank()) {
                return ParsedItem(itemName = itemName, quantity = quantity)
            }
        }

        return ParsedItem(itemName = trimmed, quantity = null)
    }
}
