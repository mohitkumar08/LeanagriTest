package com.leanagritest.ui.movielist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leanagritest.R
import com.leanagritest.core.Utils
import com.leanagritest.databinding.ActivityMoviesListBinding
import com.leanagritest.repository.local.entity.MovieModel
import com.leanagritest.ui.moviedetails.MovieDetailsActivity
import com.leanagritest.ui.movielist.adapter.MoviesListAdapter
import com.leanagritest.ui.movielist.adapter.OnMoviesSelectListener

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
    private val visibleThreshold = 10
    private var lastVisibleItem = 0
    private var loading = false
    private lateinit var binding: ActivityMoviesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies_list)
        binding.viewModel = viewModel
        binding
        initView()
        addObserver()
        viewModel.getMovies()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            isSmoothScrollbarEnabled = true
        }
        binding.recyclerView.adapter = moviesListAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = moviesListAdapter.itemCount
                lastVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (loading.not() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (Utils.isInternetAvailable()) {
                        binding.progressBar.visibility = View.VISIBLE
                        loading = true
                        viewModel.getMovies()
                    } else {
                        loading = false
                    }
                }
            }
        })
    }

    private fun addObserver() {
        viewModel.listOfMoviesLiveData.observe(this, { list ->
            list?.let {
                moviesListAdapter.addMovies(it)
                loading = false
            }
        })
        viewModel.apiCallStatusLiveData.observe(this, {
            binding.progressBar.visibility = View.GONE
        })
        viewModel.clearListLiveData.observe(this, {
            binding.progressBar.visibility = View.VISIBLE
            moviesListAdapter.clear()
            loading = true
        })

    }

    override fun onClickMovie(movie: MovieModel) {
        MovieDetailsActivity.showDetailsOfMovie(this, movie)
    }
}