package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpdateWordDetectorUseCase @Inject constructor(
    private val wordDetectorRepo: WordDetectorRepo
) {

    suspend operator fun invoke(model: WordDetectorModel) {
        return wordDetectorRepo.update(model)
    }
}