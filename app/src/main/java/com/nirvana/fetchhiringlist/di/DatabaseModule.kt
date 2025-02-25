package com.nirvana.fetchhiringlist.di

import android.app.Application
import androidx.room.Room
import com.nirvana.feature_fetchhiring.model.HiringDao
import com.nirvana.fetchhiringlist.FetchHiringDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideHiringDao(database: FetchHiringDatabase): HiringDao {
        return database.hiringDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): FetchHiringDatabase {
        return Room.databaseBuilder(
            app,
            FetchHiringDatabase::class.java,
            "fetch_hiring_database"
        ).fallbackToDestructiveMigration().build()
    }
}