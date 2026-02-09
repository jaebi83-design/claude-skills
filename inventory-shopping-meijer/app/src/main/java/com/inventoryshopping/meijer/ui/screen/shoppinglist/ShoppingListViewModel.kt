package com.inventoryshopping.meijer.ui.screen.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import com.inventoryshopping.meijer.domain.usecase.AddItemToListUseCase
import com.inventoryshopping.meijer.domain.usecase.AssignItemToAisleUseCase
import com.inventoryshopping.meijer.domain.usecase.CheckOffItemUseCase
import com.inventoryshopping.meijer.domain.usecase.ImportFromClipboardUseCase
import com.inventoryshopping.meijer.domain.usecase.SortListByAisleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val storeRepository: StoreRepository,
    private val addItemToListUseCase: AddItemToListUseCase,
    private val sortListByAisleUseCase: SortListByAisleUseCase,
    private val checkOffItemUseCase: CheckOffItemUseCase,
    private val assignItemToAisleUseCase: AssignItemToAisleUseCase,
    private val importFromClipboardUseCase: ImportFromClipboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    fun loadList(listId: String) {
        viewModelScope.launch {
            val list = shoppingListRepository.getListById(listId) ?: return@launch
            val storeId = list.storeId
            _uiState.update { it.copy(list = list, storeId = storeId) }

            // Load available sections for aisle assignment
            val sections = storeRepository.getSectionsByStore(storeId)
            _uiState.update { it.copy(availableSections = sections) }

            // Observe sorted items
            sortListByAisleUseCase.observe(listId, storeId).collect { sortedResult ->
                _uiState.update { it.copy(sortedResult = sortedResult, isLoading = false) }
            }
        }
    }

    fun updateNewItemText(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val text = _uiState.value.newItemText.trim()
        val listId = _uiState.value.list?.listId ?: return
        val storeId = _uiState.value.storeId ?: return
        if (text.isBlank()) return

        viewModelScope.launch {
            addItemToListUseCase(text, listId, storeId)
            _uiState.update { it.copy(newItemText = "") }
        }
    }

    fun toggleItemChecked(itemId: String, isChecked: Boolean) {
        viewModelScope.launch {
            checkOffItemUseCase(itemId, isChecked)
        }
    }

    fun showAssignAisleSheet(itemId: String, itemName: String) {
        _uiState.update {
            it.copy(
                showAssignAisleSheet = true,
                selectedItemId = itemId,
                selectedItemName = itemName
            )
        }
    }

    fun dismissAssignAisleSheet() {
        _uiState.update {
            it.copy(
                showAssignAisleSheet = false,
                selectedItemId = null,
                selectedItemName = null
            )
        }
    }

    fun assignItemToAisle(sectionId: String) {
        val itemId = _uiState.value.selectedItemId ?: return
        val storeId = _uiState.value.storeId ?: return

        viewModelScope.launch {
            assignItemToAisleUseCase(itemId, sectionId, storeId)
            dismissAssignAisleSheet()
        }
    }

    fun importFromClipboard(clipboardText: String) {
        val listId = _uiState.value.list?.listId ?: return
        val storeId = _uiState.value.storeId ?: return

        viewModelScope.launch {
            importFromClipboardUseCase(clipboardText, listId, storeId)
        }
    }
}
