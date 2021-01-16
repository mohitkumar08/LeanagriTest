package com.leanagritest.ui.moviedetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leanagritest.ApiCallStatus
import com.leanagritest.R
import com.leanagritest.databinding.ActivityMovieDetailsBinding
import com.leanagritest.repository.local.entity.MovieModel

const val ARG_MOVIE_DATA = "movie_data"

class MovieDetailsActivity : AppCompatActivity() {
    companion object {
        fun showDetailsOfMovie(activity: Activity, movieData: MovieModel) {
            Intent(activity, MovieDetailsActivity::class.java).apply {
                putExtra(ARG_MOVIE_DATA, movieData)
            }.run {
                activity.startActivity(this)
            }
        }
    }

    private val viewModel: MovieDetailsViewModel by lazy {
        ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
    }
    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        addObserver()
        intent.getParcelableExtra<MovieModel>(ARG_MOVIE_DATA)?.let {
            viewModel.getMovieDetails(it)
        }
    }

    private fun addObserver() {
        viewModel.movieDetailLiveData.observe(this, {

        })
        viewModel.apiCallStatusLiveData.observe(this, {
            if (ApiCallStatus.NOT_FOUND == it) {
                Toast.makeText(this, getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
            }
        })

    }
}