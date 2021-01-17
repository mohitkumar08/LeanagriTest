package com.leanagritest.ui.movielist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.leanagritest.core.AppObjectController
import com.leanagritest.core.Utils
import com.leanagritest.repository.local.entity.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    private val _listOfMoviesMutableLiveData: MutableLiveData<List<MovieModel>> = MutableLiveData()
    val listOfMoviesLiveData: LiveData<List<MovieModel>> = _listOfMoviesMutableLiveData
    private var pageNumber = 1
    private var totalPage = 1

    private val queryMap: HashMap<String, String> = HashMap<String, String>()

    init {
        queryMap.apply {
            put("language", "en-US")
      //      put("sort_by", "release_date.desc")
            put("page", "1")
            put("primary_release_date.gte","2014-09-15")
            put("primary_release_date.lte","2019-01-14")

        }
    }

////URL: /discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22
    fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetAvailable()) {
                getFromServer()
            } else {
                getFromLocalDb()
            }
        }
    }

    private fun getFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val responseObj =
                    AppObjectController.moviesNetworkService.getMovies(getQueryParams())
                _listOfMoviesMutableLiveData.postValue(responseObj.results)
                pageNumber = responseObj.page
                totalPage = responseObj.totalPages
                saveInDbForOffline(responseObj.results)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private suspend fun getFromLocalDb() {
        _listOfMoviesMutableLiveData.postValue(
            AppObjectController.appDatabase.moviesDao().getAllMovies()
        )
    }

    private suspend fun saveInDbForOffline(results: List<MovieModel>) {
        AppObjectController.appDatabase.moviesDao().addMoviesInDb(results)
    }

    private fun getQueryParams(): HashMap<String, String> {
        queryMap["page"] = pageNumber.toString()
        queryMap["sort_by"] = pageNumber.toString()
        return queryMap
    }
}
