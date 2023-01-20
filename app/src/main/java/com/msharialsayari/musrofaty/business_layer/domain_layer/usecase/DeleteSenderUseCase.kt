package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeleteSenderUseCase  @Inject constructor(
    private val senderRepo: SenderRepo,
    private val deleteSenderSmsUseCase: DeleteSenderSmsUseCase
) {

    suspend operator fun invoke(id: Int) {
        senderRepo.delete(id)
        deleteSenderSmsUseCase.invoke(id)
    }


}