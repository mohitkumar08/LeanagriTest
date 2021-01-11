package com.leanagritest.repository.local.dao

import androidx.room.*
import com.leanagritest.repository.local.entity.MovieModel


@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMoviesInDb(movieList: List<MovieModel>)

    @Query(value = "SELECT * FROM movie_table")
    suspend fun getAllMovies(): List<MovieModel>
}