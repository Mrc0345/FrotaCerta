package com.marcus.frotacerta.data.local

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context = context.applicationContext,
                klass = AppDatabase::class.java,
                name = "frota_certa.db"
            )
                .setDriver(AndroidSQLiteDriver())
                .build()

            INSTANCE = instance

            instance
        }
    }
}
