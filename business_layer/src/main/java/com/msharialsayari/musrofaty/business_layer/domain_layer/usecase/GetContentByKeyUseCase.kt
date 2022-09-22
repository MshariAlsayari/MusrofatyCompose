package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetContentByKeyUseCase @Inject constructor(
    private val contentRepo: ContentRepo
) {

    suspend operator fun invoke(contentKet: ContentKey): List<ContentModel> {
        return contentRepo.getContentByKey(contentKet.name)

    }
}