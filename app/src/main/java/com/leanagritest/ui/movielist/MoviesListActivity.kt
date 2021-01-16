package com.leanagritest.ui.movielist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.leanagritest.R
import com.leanagritest.repository.local.entity.MovieModel
import com.leanagritest.ui.moviedetails.MovieDetailsActivity
import com.leanagritest.ui.movielist.adapter.MoviesListAdapter
import com.leanagritest.ui.movielist.adapter.OnMoviesSelectListener
import kotlinx.android.synthetic.main.activity_movies_list.*

class MoviesListActivity : AppCompatActivity(), OnMoviesSelectListener {

    companion object {
        fun startMoviesListActivity(activity: Activity) {
            Intent(activity, MoviesListActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }.run {
                activity.startActivity(this)
            }
        }
    }

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProvider(this).get(MovieListViewModel::class.java)
    }
    private var moviesListAdapter: MoviesListAdapter = MoviesListAdapter(listener = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)
        initView()
        addObserver()
        viewModel.getMovies()
    }

    private fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(this).apply {
            isSmoothScrollbarEnabled = true
        }
        recycler_view.adapter = moviesListAdapter
    }

    private fun addObserver() {
        viewModel.listOfMoviesLiveData.observe(this, { list ->
            list?.let {
                moviesListAdapter.addMovies(it)
            }
        })
    }

    override fun onClickMovie(movie: MovieModel) {
        MovieDetailsActivity.showDetailsOfMovie(this, movie)
    }
}