package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ShareRequest
import kotlinx.coroutines.flow.Flow

interface PushRepository {
    fun shareContent(shareRequest: ShareRequest): Flow<Resource>

}

