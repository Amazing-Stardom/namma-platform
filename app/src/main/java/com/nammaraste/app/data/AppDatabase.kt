package com.nammaraste.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Road::class, DamageReport::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roadDao(): RoadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_raste_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.roadDao()
                    val dummyRoads = listOf(
                        Road(roadName = "MG Road", location = "Bengaluru", contractorName = "XYZ Construction", contactNumber = "9876543210", warrantyPeriod = "5 Years", healthScore = 90),
                        Road(roadName = "Brigade Road", location = "Bengaluru", contractorName = "ABC Builders", contactNumber = "9876543211", warrantyPeriod = "3 Years", healthScore = 40),
                        Road(roadName = "Outer Ring Road", location = "Bengaluru", contractorName = "LMN Infrastructure", contactNumber = "9876543212", warrantyPeriod = "10 Years", healthScore = 80),
                        Road(roadName = "Hosur Road", location = "Bengaluru", contractorName = "DEF Developers", contactNumber = "9876543213", warrantyPeriod = "2 Years", healthScore = 45),
                        Road(roadName = "Tumkur Road", location = "Bengaluru", contractorName = "GHI Corp", contactNumber = "9876543214", warrantyPeriod = "7 Years", healthScore = 75)
                    )
                    dao.insertRoads(dummyRoads)
                }
            }
        }
    }
}
