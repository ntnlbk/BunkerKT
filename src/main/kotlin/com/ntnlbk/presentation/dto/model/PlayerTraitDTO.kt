package com.ntnlbk.presentation.dto.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerTraitDTO(
    val value: String,
    val isOpen: Boolean = false
)
