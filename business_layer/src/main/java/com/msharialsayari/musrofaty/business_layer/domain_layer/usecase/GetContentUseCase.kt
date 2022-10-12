package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetContentUseCase @Inject constructor(
    private val contentRepo: ContentRepo
) {

    suspend operator fun invoke(id: Int): ContentModel? {
        return contentRepo.getContentById(id)

    }
}