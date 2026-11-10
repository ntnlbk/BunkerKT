package com.ntnlbk.domain.model

sealed class HealthScore {
    data object NotApplicable : HealthScore()
    data class Value(
        val score: Int
    ) : HealthScore()
}