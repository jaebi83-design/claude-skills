package com.inventoryshopping.meijer.di

import android.content.Context
import androidx.room.Room
import com.inventoryshopping.meijer.data.local.AppDatabase
import com.inventoryshopping.meijer.data.local.dao.CategorySectionDefaultDao
import com.inventoryshopping.meijer.data.local.dao.ProductAisleMappingDao
import com.inventoryshopping.meijer.data.local.dao.ShoppingListDao
import com.inventoryshopping.meijer.data.local.dao.ShoppingListItemDao
import com.inventoryshopping.meijer.data.local.dao.StoreDao
import com.inventoryshopping.meijer.data.local.dao.StoreSectionDao
import com.inventoryshopping.meijer.data.repository.AisleMappingRepositoryImpl
import com.inventoryshopping.meijer.data.repository.ShoppingListRepositoryImpl
import com.inventoryshopping.meijer.data.repository.StoreRepositoryImpl
import com.inventoryshopping.meijer.domain.repository.AisleMappingRepository
import com.inventoryshopping.meijer.domain.repository.ShoppingListRepository
import com.inventoryshopping.meijer.domain.repository.StoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory_shopping_meijer.db"
        ).build()
    }

    @Provides
    fun provideStoreDao(db: AppDatabase): StoreDao = db.storeDao()

    @Provides
    fun provideStoreSectionDao(db: AppDatabase): StoreSectionDao = db.storeSectionDao()

    @Provides
    fun provideProductAisleMappingDao(db: AppDatabase): ProductAisleMappingDao =
        db.productAisleMappingDao()

    @Provides
    fun provideCategorySectionDefaultDao(db: AppDatabase): CategorySectionDefaultDao =
        db.categorySectionDefaultDao()

    @Provides
    fun provideShoppingListDao(db: AppDatabase): ShoppingListDao = db.shoppingListDao()

    @Provides
    fun provideShoppingListItemDao(db: AppDatabase): ShoppingListItemDao =
        db.shoppingListItemDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(
        impl: ShoppingListRepositoryImpl
    ): ShoppingListRepository

    @Binds
    @Singleton
    abstract fun bindAisleMappingRepository(
        impl: AisleMappingRepositoryImpl
    ): AisleMappingRepository

    @Binds
    @Singleton
    abstract fun bindStoreRepository(
        impl: StoreRepositoryImpl
    ): StoreRepository
}
