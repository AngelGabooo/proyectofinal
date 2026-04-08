package com.angel.proyectofinal.features.routines.data.datasources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.RoutineEntity
import com.angel.proyectofinal.features.routines.data.datasources.local.entities.WorkoutSessionEntity

@Database(
    entities = [RoutineEntity::class, WorkoutSessionEntity::class],
    version =4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitpro_up_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}