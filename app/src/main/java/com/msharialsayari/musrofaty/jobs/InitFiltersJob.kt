package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.LogicOperators
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitFiltersJob@AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val filtersRepo: FilterRepo,

    ) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {

        val advancedFilters = filtersRepo.getAll()

        advancedFilters.map {
            val filterMode = FilterModel(
                id = it.id,
                title = it.title,
                senderId = it.senderId
            )

            val words= FilterAdvancedModel.getFilterWordsAsList(it.words)

           val filterWords =  words.map { word->
               FilterWordModel(
                   filterId = it.id,
                   word = word,
                   logicOperator = LogicOperators.AND
               )
            }

            filtersRepo.saveFilter(filterMode)
            filtersRepo.saveFilterWords(*filterWords.toTypedArray())

        }


        return Result.success()
    }
}