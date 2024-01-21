package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import kotlinx.parcelize.Parcelize


@Parcelize
data class CategoryModel(
    var id: Int=0,
    var valueAr: String?=null,
    var valueEn: String?=null,
    var isSelected: Boolean = false,
    var isDefault: Boolean = false,
    var sortOrder: Int = 0,
) : Parcelable{


    companion object{
        fun getDisplayName(context: Context, model:CategoryModel?):String {
            return if (SharedPreferenceManager.isArabic(context)){
                model?.valueAr?: "غير محدد"
            }else{
                model?.valueEn ?: "No Category"
            }
        }

        fun getDisplayName(context: Context, model:CategoryEntity?):String {
            return if (SharedPreferenceManager.isArabic(context)){
                model?.valueAr?: "غير محدد"
            }else{
                model?.valueEn ?: "No Category"
            }
        }

        fun getNoSelectedCategory():CategoryModel {
            return CategoryModel(
                id = -1,
                valueAr = "غير محدد",
                valueEn = "No Category",
                isSelected = false,
                isDefault = false,
                sortOrder = 0
            )
        }
    }

}



fun CategoryModel.toCategoryEntity() = CategoryEntity(id, valueAr, valueEn,isDefault= isDefault, sortOrder = sortOrder)