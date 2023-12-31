package com.carlos.movies_app.data

import com.carlos.movies_app.data.local.movie.MovieDatabase
import com.carlos.movies_app.data.mappers.toMovie
import com.carlos.movies_app.data.mappers.toMovieEntity
import com.carlos.movies_app.data.remote.MovieApi
import com.carlos.movies_app.domain.model.Movie
import com.carlos.movies_app.domain.repository.MovieListRepository
import com.carlos.movies_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
       return flow {
           emit(Resource.Loading(true))

           val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

           val shouldLoadLocalMovie = localMovieList.isEmpty() && !forceFetchFromRemote

           if (shouldLoadLocalMovie) {
               emit(Resource.Success(
                   data = localMovieList.map { movieEntity ->
                       movieEntity.toMovie(category)
                   }
               ))

               emit(Resource.Loading(false))
               return@flow

               }


               val movieListFromApi = try {
                   movieApi.getMoviesList(category, page)

               } catch (e: IOException) {
                   e.printStackTrace()
                   emit(Resource.Error(message = "Error loading movies"))
                   return@flow
               } catch (e: HttpException) {
                   e.printStackTrace()
                   emit(Resource.Error(message = "Error loading Movies"))
                   return@flow
               } catch (e: Exception) {
                   e.printStackTrace()
                   emit(Resource.Error(message = "Error loading Movies"))
                   return@flow
               }

           val movieEntities = movieListFromApi.result.let {
               it.map { movieDto ->
                   movieDto.toMovieEntity(category)

               }
           }

           movieDatabase.movieDao.upsertMovieList(movieEntities)


           emit(Resource.Success(
               movieEntities.map {
                   it.toMovie(category)
               }
           ))
       }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
       return flow {
           emit(Resource.Loading(true))

           val movieEntity = movieDatabase.movieDao.getMovieById(id)

           if (movieEntity != null) {
               emit(
                   Resource.Success(movieEntity.toMovie(movieEntity.category))
               )

               emit(Resource.Loading(false))
               return@flow
           }

           emit(Resource.Error("Error no such movie"))

           emit(Resource.Loading(false))
       }
    }
}