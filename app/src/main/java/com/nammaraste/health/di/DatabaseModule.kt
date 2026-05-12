package com.nammaraste.health.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nammaraste.health.data.local.AppDatabase
import com.nammaraste.health.data.local.dao.ContractorDao
import com.nammaraste.health.data.local.dao.DamageReportDao
import com.nammaraste.health.data.local.dao.RoadDao
import com.nammaraste.health.utils.SeedDataHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        databaseProvider: Provider<AppDatabase>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                SeedDataHelper.seed(databaseProvider.get())
            }
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                SeedDataHelper.seed(databaseProvider.get())
            }
        }).build()
    }

    @Provides
    fun provideRoadDao(db: AppDatabase): RoadDao = db.roadDao()

    @Provides
    fun provideDamageReportDao(db: AppDatabase): DamageReportDao = db.damageReportDao()

    @Provides
    fun provideContractorDao(db: AppDatabase): ContractorDao = db.contractorDao()
}
