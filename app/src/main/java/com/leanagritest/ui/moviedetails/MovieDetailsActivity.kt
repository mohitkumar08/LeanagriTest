package com.leanagritest.ui.moviedetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.leanagritest.ApiCallStatus
import com.leanagritest.R
import com.leanagritest.core.Utils.dpToPx
import com.leanagritest.core.Utils.getMovieInFormat
import com.leanagritest.core.Utils.getMovieTimeInPattern
import com.leanagritest.core.Utils.setImage
import com.leanagritest.databinding.ActivityMovieDetailsBinding
import com.leanagritest.repository.local.entity.MovieModel
import com.leanagritest.repository.server.Genre

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
            it?.let {
                binding.tvMovieName.text = it.originalTitle
                binding.image.setImage(it.getBackgroundImage())
                binding.tvAbout.text = it.overview
                binding.tvDuration.text = it.runtime.getMovieTimeInPattern()
                binding.tvReleaseDate.text = it.releaseDate.getMovieInFormat()
                binding.tvVoteCount.text = getString(R.string.votes, it.voteAverage.toString())
                addRatingInRatingView(it.voteAverage)
                addGenres(it.genres)
            }
        })
        viewModel.apiCallStatusLiveData.observe(this, {
            if (ApiCallStatus.NOT_FOUND == it) {
                Toast.makeText(this, getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addRatingInRatingView(voteAverage: Double) {
        val ratingValue = voteAverage / 2
        binding.ratingBar.rating = ratingValue.toFloat()
        binding.ratingBar.stepSize = 0.5F
    }


    private fun addGenres(genres: List<Genre>) {
        genres.forEach {
            binding.llGenres.addView(getTextView(it.name))
        }
    }

    private fun getTextView(text: String): AppCompatTextView {
        val textView = AppCompatTextView(this)
        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView.setBackgroundResource(R.drawable.rect_round)
        textView.text = text
        textView.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 0, 10, 0)
        textView.layoutParams = params
        return textView
    }

}