package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.ShareRequest
import com.drax.sendit.domain.network.model.ShareResponse
import kotlinx.coroutines.flow.Flow

interface PushRepository {
    fun shareContent(shareRequest: ShareRequest): Flow<Resource<ApiResponse<ShareResponse>>>

}

