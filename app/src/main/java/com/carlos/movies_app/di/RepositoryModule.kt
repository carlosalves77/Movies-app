package com.carlos.movies_app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    // TODO - Finish Repository Module
    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImpl:
    )
}