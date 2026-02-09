package com.inventoryshopping.meijer.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.inventoryshopping.meijer.ui.theme.CheckedItemColor

@Composable
fun ShoppingItemRow(
    itemName: String,
    quantity: String?,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = itemName,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                color = if (isChecked) CheckedItemColor else MaterialTheme.colorScheme.onSurface
            )
            if (quantity != null) {
                Text(
                    text = quantity,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isChecked) CheckedItemColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
