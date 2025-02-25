package com.nirvana.feature_fetchhiring.model

data class HiringDTO(
    val id: Int,
    val listId: Int,
    val name: String?
) : BaseDTO()