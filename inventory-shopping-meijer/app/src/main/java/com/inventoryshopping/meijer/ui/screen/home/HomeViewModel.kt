package com.inventoryshopping.meijer.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val store = storeRepository.getDefaultStore()
            _uiState.update { it.copy(defaultStoreId = store?.storeId) }
        }
        viewModelScope.launch {
            shoppingListRepository.observeActiveLists().collect { lists ->
                _uiState.update { it.copy(lists = lists, isLoading = false) }
            }
        }
    }

    fun showNewListDialog() {
        _uiState.update { it.copy(showNewListDialog = true) }
    }

    fun dismissNewListDialog() {
        _uiState.update { it.copy(showNewListDialog = false) }
    }

    fun createNewList(name: String) {
        val storeId = _uiState.value.defaultStoreId ?: return
        viewModelScope.launch {
            val list = ShoppingListEntity(
                name = name.ifBlank { "Shopping List" },
                storeId = storeId
            )
            shoppingListRepository.createList(list)
            _uiState.update { it.copy(showNewListDialog = false) }
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            shoppingListRepository.deleteList(listId)
        }
    }
}
