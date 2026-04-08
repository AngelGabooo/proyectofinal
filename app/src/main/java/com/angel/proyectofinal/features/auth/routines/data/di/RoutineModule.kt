package com.angel.proyectofinal.core.di

import android.content.Context
import com.angel.proyectofinal.features.routines.data.datasources.local.dao.RoutineDao
import com.angel.proyectofinal.features.routines.data.datasources.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoutineModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideRoutineDao(database: AppDatabase): RoutineDao = database.routineDao()
}