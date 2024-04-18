package com.github.lamba92.ui.model

import com.github.lamba92.data.WorldTimeApiResponse

sealed interface ResourceStatus {
    data object NotLoaded : ResourceStatus
    data object Loading : ResourceStatus
    data class Success(val data: WorldTimeApiResponse) : ResourceStatus
    data class Error(val message: String) : ResourceStatus
}