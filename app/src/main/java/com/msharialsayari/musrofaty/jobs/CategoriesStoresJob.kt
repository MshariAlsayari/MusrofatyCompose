package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsContainsStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirestoreUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CategoriesStoresJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val getAllSmsContainsStoreUseCase: GetAllSmsContainsStoreUseCase,
    private val categoryRepo:CategoryRepo,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val postStoreToFirestoreUseCase: PostStoreToFirestoreUseCase,
    ) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private val TAG = CategoriesStoresJob::class.java.simpleName
    }

    var categories = emptyList<CategoryEntity>()


    override suspend fun doWork(): Result {
        categories = categoryRepo.getCategoriesList()
        if(categories.isNotEmpty()){
            categoriesHospitals()
            categoriesPharmacies()
            categoriesHotels()
            categoriesRestaurants()
            categoriesCoffee()
            categoriesMcdonaldsRestaurant()
            categoriesDunkinCoffee()
            categoriesSuperMarkets()
            categoriesHyperMarkets()
            categoriesTamimiSuperMarket()
            categoriesDanubeSuperMarket()
        }
        return Result.success()
    }

    private suspend fun categoriesHospitals(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.HOSPITALS.search)
        Log.d(TAG , "categoriesHospitals() size:${list.size}")
        list.filter {
           it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.HOSPITALS.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }
    }

    private suspend fun categoriesPharmacies(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.PHARMACIES.search)
        Log.d(TAG , "categoriesPharmacies() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.PHARMACIES.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }

    }

    private suspend fun categoriesHotels(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.HOTELS.search)
        Log.d(TAG , "categoriesHotels() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.HOTELS.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }

    }

    private suspend fun categoriesRestaurants(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.RESTAURANTS.search)
        Log.d(TAG , "categoriesRestaurants() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.RESTAURANTS.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }
    }

    private suspend fun categoriesCoffee(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.COFFEES.search)
        Log.d(TAG , "categoriesCoffee() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.COFFEES.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }
    }

    private suspend fun categoriesMcdonaldsRestaurant(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.MCDONALDSRESTURANT.search)
        Log.d(TAG , "categoriesMcdonaldsRestaurant() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.MCDONALDSRESTURANT.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }

    }

    private suspend fun categoriesDunkinCoffee(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.DUNKINDONUTS.search)
        Log.d(TAG , "categoriesDunkinCoffee() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.DUNKINDONUTS.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }

    }

    private suspend fun categoriesSuperMarkets(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.SUPER_MARKETS.search).toMutableList()
        list.addAll(getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.SUPERMARKETS.search))
        Log.d(TAG , "categoriesSuperMarkets() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.SUPER_MARKETS.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }

    }

    private suspend fun categoriesHyperMarkets(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.HYPERMARKETS.search).toMutableList()
        list.addAll(getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.HYPER_MARKETS.search))
        Log.d(TAG , "categoriesHyperMarkets() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.HYPERMARKETS.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }
    }

    private suspend fun categoriesTamimiSuperMarket(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.TAMIMISUPERMARKET.search)
        Log.d(TAG , "categoriesTamimiSuperMarket() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.TAMIMISUPERMARKET.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }
    }

    private suspend fun categoriesDanubeSuperMarket(){
        val list = getAllSmsContainsStoreUseCase.invoke(storeName = StoreWithCategory.DANUBESUPERMARKET.search)
        Log.d(TAG , "categoriesDanubeSuperMarket() size:${list.size}")
        list.filter {
            it.storeAndCategoryModel == null || it.storeAndCategoryModel?.category == null
        }.map {
            val storeName = it.storeName
            val storeModel = StoreModel(name = storeName, categoryId = StoreWithCategory.DANUBESUPERMARKET.categoryId)
            addOrUpdateStoreUseCase.invoke(storeModel)
            postStoreToFirestoreUseCase.invoke(storeModel.toStoreEntity())
        }
    }


}

enum class StoreWithCategory(val search:String, val categoryId:Int){
    COFFEES("coffee", 3),
    DUNKINDONUTS("dunkin" , 3),
    RESTAURANTS("restaurant", 5),
    MCDONALDSRESTURANT("MCDONALDS" , 5),
    PHARMACIES("pharmacy", 6),
    SUPERMARKETS("supermarket" , 8),
    SUPER_MARKETS("super market" , 8),
    HYPERMARKETS("hypermarket" , 8),
    HYPER_MARKETS("hyper market" , 8),
    TAMIMISUPERMARKET("tamimi" , 8),
    DANUBESUPERMARKET("danube" , 8),
    HOTELS("hotel", 9),
    HOSPITALS("hospital",13),

}