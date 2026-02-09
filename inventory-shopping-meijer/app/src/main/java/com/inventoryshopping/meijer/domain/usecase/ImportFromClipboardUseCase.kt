package com.inventoryshopping.meijer.domain.usecase

import javax.inject.Inject

class ImportFromClipboardUseCase @Inject constructor(
    private val addItemToListUseCase: AddItemToListUseCase
) {
    suspend operator fun invoke(clipboardText: String, listId: String, storeId: String): Int {
        val lines = clipboardText
            .lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { line ->
                // Strip common list prefixes like "- ", "* ", "1. ", "• "
                line.replace(Regex("^[-*•]\\s*"), "")
                    .replace(Regex("^\\d+\\.\\s*"), "")
                    .trim()
            }
            .filter { it.isNotBlank() }

        for (line in lines) {
            addItemToListUseCase(line, listId, storeId)
        }

        return lines.size
    }
}
