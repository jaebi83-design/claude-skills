package com.inventoryshopping.meijer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inventoryshopping.meijer.data.local.dao.CategorySectionDefaultDao
import com.inventoryshopping.meijer.data.local.dao.ProductAisleMappingDao
import com.inventoryshopping.meijer.data.local.dao.ShoppingListDao
import com.inventoryshopping.meijer.data.local.dao.ShoppingListItemDao
import com.inventoryshopping.meijer.data.local.dao.StoreDao
import com.inventoryshopping.meijer.data.local.dao.StoreSectionDao
import com.inventoryshopping.meijer.data.local.entity.CategorySectionDefaultEntity
import com.inventoryshopping.meijer.data.local.entity.ProductAisleMappingEntity
import com.inventoryshopping.meijer.data.local.entity.ShoppingListEntity
import com.inventoryshopping.meijer.data.local.entity.ShoppingListItemEntity
import com.inventoryshopping.meijer.data.local.entity.StoreEntity
import com.inventoryshopping.meijer.data.local.entity.StoreSectionEntity

@Database(
    entities = [
        StoreEntity::class,
        StoreSectionEntity::class,
        ProductAisleMappingEntity::class,
        CategorySectionDefaultEntity::class,
        ShoppingListEntity::class,
        ShoppingListItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun storeSectionDao(): StoreSectionDao
    abstract fun productAisleMappingDao(): ProductAisleMappingDao
    abstract fun categorySectionDefaultDao(): CategorySectionDefaultDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingListItemDao(): ShoppingListItemDao
}
