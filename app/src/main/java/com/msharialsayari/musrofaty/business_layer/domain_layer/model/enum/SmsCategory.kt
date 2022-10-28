package com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum

import android.content.Context
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel


enum class SmsCategory(val valueAr: String, val valueEn: String) {
    NO_CATEGORY("غير محدد", "No category"),
    RESTAURANT("مطاعم", "Restaurant"),
    COFFEE("كوفيات", "Coffee"),
    SUPER_MARKET("سوبرماركت", "Super Market"),
    MOBILE_BILLS("فاتورة جوال", "Mobile Bill"),
    WATER_BILLS("فاتورة ماء", "Water Bill"),
    ELECTRIC_BILLS("فاتورة كهرباء", "Electric Bill"),
    HOSPITAL("مستشفى", "Hospital"),
    PHARMACY("صيدلية", "Pharmacy"),
    GAS_STATION("محطة بنزين", "Gas Station"),
    LAUNDRY("مغسلة ملابس", "Laundry"),
    BARBER_SHOP("حلاق", "Barber Shop");

    companion object {


        fun getAll(): List<CategoryModel> {
            val list = mutableListOf<CategoryModel>()
            values().forEach {
                list.add(
                    CategoryModel(
                        valueAr = it.valueAr,
                        valueEn = it.valueEn,
                        isDefault = true
                    )
                )

            }

            return list
        }


        private fun getNoCategory(): Int {
            return R.string.sms_category_no_category
        }

        fun getContent(category: CategoryModel?, context: Context): String? {
            return if (SharedPreferenceManager.isArabic(context)) {

                if (category?.id == -1) {
                    context.getString(getNoCategory())
                } else {
                    category?.valueAr
                }
            } else {
                if (category?.id == -1) {
                    context.getString(getNoCategory())
                } else {
                    category?.valueEn
                }
            }
        }
    }


}