package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetWordDetectorUseCase @Inject constructor(
    private val wordDetectorRepo: WordDetectorRepo
) {

     operator fun invoke(type: WordDetectorType): Flow<List<WordDetectorEntity>> {
        return wordDetectorRepo.getAllFlowList(type)
    }
}