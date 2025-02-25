package com.nirvana.feature_fetchhiring.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hiring_data")
data class HiringEntity(
    @PrimaryKey
    val id: Int,
    val listId: Int,
    val name: String?
) {
    companion object {
        fun fromDTO(dto: HiringDTO) = HiringEntity(
            id = dto.id,
            listId = dto.listId,
            name = dto.name,
        )
    }

    fun toDTO() = HiringDTO(
        id = id,
        listId = listId,
        name = name
    )
}