package com.nammaraste.health.utils

import com.nammaraste.health.data.local.AppDatabase
import com.nammaraste.health.data.local.entity.ContractorEntity
import com.nammaraste.health.data.local.entity.RoadEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SeedDataHelper {
    fun seed(db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            if (db.roadDao().getRoadCount() == 0) {
                val roads = listOf(
                    RoadEntity(1, "Hosur–Bagalur Main Road", "Hosur Taluka, Krishnagiri District", 12.5f, 2019, 45, 12.7409, 77.8253, 12.8012, 77.8456),
                    RoadEntity(2, "Denkanikottai–Thally Road", "Denkanikottai Taluka, Krishnagiri", 8.3f, 2021, 78, 12.5658, 77.9924, 12.6102, 78.0215),
                    RoadEntity(3, "Shoolagiri–Palacode Road", "Shoolagiri Taluka, Dharmapuri", 15.7f, 2020, 30, 12.6520, 78.3401, 12.7234, 78.4102),
                    RoadEntity(4, "Bargur–Anchetti Link Road", "Bargur, Krishnagiri District", 6.2f, 2022, 90, 12.3301, 78.2101, 12.3712, 78.2598),
                    RoadEntity(5, "Veppanapalli–Uthangarai Road", "Veppanapalli, Krishnagiri", 10.4f, 2018, 20, 12.4501, 78.1250, 12.5123, 78.1834)
                )
                db.roadDao().insertAllRoads(roads)

                val contractors = listOf(
                    ContractorEntity(1, 1, "Sri Balaji Road Works", "+91 98765 43210", "balaji@constructions.com", "₹48,00,000", "15 March 2019", "20 August 2019", "5 Years (until 2024)", true),
                    ContractorEntity(2, 2, "KNR Constructions Ltd", "+91 99887 76655", "knr@constructions.com", "₹32,00,000", "10 Jan 2021", "15 May 2021", "5 Years (until 2026)", false),
                    ContractorEntity(3, 3, "Ashoka Buildcon Pvt Ltd", "+91 94440 12345", "ashoka@buildcon.com", "₹62,00,000", "01 June 2020", "30 Nov 2020", "5 Years (until 2025)", false),
                    ContractorEntity(4, 4, "Gayatri Projects Ltd", "+91 90001 55566", "gayatri@projects.com", "₹24,00,000", "12 Feb 2022", "20 July 2022", "5 Years (until 2027)", false),
                    ContractorEntity(5, 5, "NCC Limited", "+91 91234 56789", "ncc@limited.com", "₹41,00,000", "05 May 2018", "15 Oct 2018", "5 Years (until 2023)", true)
                )
                db.contractorDao().insertAllContractors(contractors)
            }
        }
    }
}
