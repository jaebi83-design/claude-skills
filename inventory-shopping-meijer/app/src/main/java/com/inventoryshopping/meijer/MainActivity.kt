package com.inventoryshopping.meijer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.inventoryshopping.meijer.ui.navigation.AppNavGraph
import com.inventoryshopping.meijer.ui.theme.InventoryShoppingMeijerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryShoppingMeijerTheme {
                AppNavGraph()
            }
        }
    }
}
