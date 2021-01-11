package com.leanagritest.ui.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leanagritest.R

class MoviesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)
    }
}
//https://developers.themoviedb.org/3/discover/movie-discover
//https://developers.themoviedb.org/3/configuration/get-api-configuration
//https://developers.themoviedb.org/3/movies/get-movie-details
///https://developers.themoviedb.org/3/movies/get-movie-credits
//https://developers.themoviedb.org/3/movies/get-similar-movies