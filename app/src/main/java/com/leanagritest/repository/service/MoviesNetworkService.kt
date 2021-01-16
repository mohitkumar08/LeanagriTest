package com.leanagritest.repository.service

import com.leanagritest.repository.server.MovieDetail
import com.leanagritest.repository.server.ResponseMovies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.HashMap

interface MoviesNetworkService {

    @GET("discover/movie")
    suspend fun getMovies(@QueryMap params: HashMap<String, String>): ResponseMovies

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @QueryMap params: Map<String, String>): Response<MovieDetail>

}