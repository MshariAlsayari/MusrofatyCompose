package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeleteFilterAmountUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {
    suspend operator fun invoke(id: Int) {
        filterRepo.deleteFilterAmount(id)
    }
}