package com.carlos.movies_app.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao

}

