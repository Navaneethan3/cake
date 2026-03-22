package com.example.constitutionmaker.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [ConstitutionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ConstitutionDatabase : RoomDatabase() {
    abstract fun constitutionDao(): ConstitutionDao

    companion object {
        @Volatile
        private var INSTANCE: ConstitutionDatabase? = null

        fun getDatabase(context: Context): ConstitutionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConstitutionDatabase::class.java,
                    "constitution_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}