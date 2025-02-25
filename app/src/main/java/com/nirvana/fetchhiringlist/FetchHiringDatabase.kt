package com.nirvana.fetchhiringlist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nirvana.feature_fetchhiring.model.HiringDao
import com.nirvana.feature_fetchhiring.model.HiringEntity

@Database(entities = [HiringEntity::class], version = 1)
abstract class FetchHiringDatabase : RoomDatabase() {
    abstract fun hiringDao(): HiringDao
}