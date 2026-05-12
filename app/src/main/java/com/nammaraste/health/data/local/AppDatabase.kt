package com.nammaraste.health.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nammaraste.health.data.local.dao.ContractorDao
import com.nammaraste.health.data.local.dao.DamageReportDao
import com.nammaraste.health.data.local.dao.RoadDao
import com.nammaraste.health.data.local.entity.ContractorEntity
import com.nammaraste.health.data.local.entity.DamageReportEntity
import com.nammaraste.health.data.local.entity.RoadEntity
import com.nammaraste.health.utils.SeedDataHelper

@Database(
    entities = [RoadEntity::class, DamageReportEntity::class, ContractorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roadDao(): RoadDao
    abstract fun damageReportDao(): DamageReportDao
    abstract fun contractorDao(): ContractorDao

    companion object {
        const val DATABASE_NAME = "namma_raste_health_db"

        val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Note: We can't easily get the AppDatabase instance here to call seed(db)
                // because it's still being built. Usually, we do this in the DI module
                // or after building the database.
            }
        }
    }
}
