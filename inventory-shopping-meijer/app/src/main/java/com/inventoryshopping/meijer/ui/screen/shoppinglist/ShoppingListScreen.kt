package com.inventoryshopping.meijer.ui.screen.shoppinglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inventoryshopping.meijer.ui.component.AisleSectionHeader
import com.inventoryshopping.meijer.ui.component.EmptyStateView
import com.inventoryshopping.meijer.ui.component.ShoppingItemRow
import com.inventoryshopping.meijer.ui.screen.assignaisle.AssignAisleContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    listId: String,
    onNavigateBack: () -> Unit,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(listId) {
        viewModel.loadList(listId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.list?.name ?: "Shopping List") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        val text = clipboardManager.getText()?.text
                        if (!text.isNullOrBlank()) {
                            viewModel.importFromClipboard(text)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Paste from Clipboard",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Add item input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.newItemText,
                    onValueChange = { viewModel.updateNewItemText(it) },
                    label = { Text("Add grocery item...") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.addItem() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider()

            val sortedResult = uiState.sortedResult
            if (sortedResult == null || (sortedResult.aisleGroups.isEmpty() && sortedResult.unknownItems.isEmpty())) {
                EmptyStateView(
                    message = "No items yet. Add groceries above."
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Render each aisle group in walk-order
                    for (group in sortedResult.aisleGroups) {
                        item(key = "header_${group.section.sectionId}") {
                            AisleSectionHeader(
                                sectionName = group.section.sectionName,
                                description = group.section.description
                            )
                        }
                        items(
                            items = group.items,
                            key = { it.itemId }
                        ) { item ->
                            ShoppingItemRow(
                                itemName = item.itemName,
                                quantity = item.quantity,
                                isChecked = item.isChecked,
                                onCheckedChange = { checked ->
                                    viewModel.toggleItemChecked(item.itemId, checked)
                                }
                            )
                        }
                    }

                    // Unknown items section at the end
                    if (sortedResult.unknownItems.isNotEmpty()) {
                        item(key = "header_unknown") {
                            AisleSectionHeader(
                                sectionName = "Unknown",
                                description = "Tap an item to assign its aisle",
                                isUnknown = true
                            )
                        }
                        items(
                            items = sortedResult.unknownItems,
                            key = { it.itemId }
                        ) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.showAssignAisleSheet(item.itemId, item.itemName)
                                    }
                            ) {
                                ShoppingItemRow(
                                    itemName = item.itemName,
                                    quantity = item.quantity,
                                    isChecked = item.isChecked,
                                    onCheckedChange = { checked ->
                                        viewModel.toggleItemChecked(item.itemId, checked)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Assign Aisle Bottom Sheet
    if (uiState.showAssignAisleSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissAssignAisleSheet() },
            sheetState = rememberModalBottomSheetState()
        ) {
            AssignAisleContent(
                itemName = uiState.selectedItemName ?: "",
                sections = uiState.availableSections,
                onSectionSelected = { sectionId ->
                    viewModel.assignItemToAisle(sectionId)
                }
            )
        }
    }
}
