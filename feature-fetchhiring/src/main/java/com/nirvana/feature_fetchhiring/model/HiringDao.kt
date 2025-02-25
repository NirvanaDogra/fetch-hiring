package com.nirvana.feature_fetchhiring.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface HiringDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHiringData(hiringData: List<HiringEntity>)

    @Query("SELECT * FROM hiring_data")
     fun getAllHiringData(): List<HiringEntity>

    @Query("DELETE FROM hiring_data")
     fun deleteAllHiringData()

    @Transaction
    fun updateHiringData(hiringData: List<HiringEntity>) {
        deleteAllHiringData()
        insertHiringData(hiringData)
    }
}