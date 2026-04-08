package com.angel.proyectofinal.core.di

import com.angel.proyectofinal.features.routines.data.repositories.RoutineRepositoryImpl
import com.angel.proyectofinal.features.routines.domain.repositories.RoutineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRoutineRepository(
        impl: RoutineRepositoryImpl
    ): RoutineRepository
}