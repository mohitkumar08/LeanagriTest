package com.leanagritest.repository.service

import com.leanagritest.repository.server.MovieDetail
import com.leanagritest.repository.server.ResponseMovies
import retrofit2.http.GET
import retrofit2.http.QueryMap

const val DIR = "3/discover/movie"

interface MoviesNetworkService {

    @GET(DIR)
    suspend fun getMovies(@QueryMap params: Map<String, String>): ResponseMovies

    @GET("$DIR/course/course_details/")
    suspend fun getMovieDetails(@QueryMap params: Map<String, String>): MovieDetail

}