package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeleteContentUseCase @Inject constructor(
    private val contentRepo: ContentRepo
) {

    suspend operator fun invoke(id: Int) {
        return contentRepo.delete(id)

    }
}