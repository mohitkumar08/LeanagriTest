package com.leanagritest.ui.movielist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.leanagritest.ApiCallStatus
import com.leanagritest.SortBased
import com.leanagritest.core.AppObjectController
import com.leanagritest.core.Utils
import com.leanagritest.repository.local.entity.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    private val _listOfMoviesMutableLiveData: MutableLiveData<List<MovieModel>> = MutableLiveData()
    val listOfMoviesLiveData: LiveData<List<MovieModel>> = _listOfMoviesMutableLiveData
    val apiCallStatusLiveData: MutableLiveData<ApiCallStatus> = MutableLiveData()
     val clearListLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var sortBased: SortBased = SortBased.EMPTY
    private var pageNumber = 1
    private var totalPage = 1

    private val queryMap: HashMap<String, String> = HashMap<String, String>()

    init {
        queryMap.apply {
            put("language", "en-US")
            put("page", "1")
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
                pageNumber++
                apiCallStatusLiveData.postValue(ApiCallStatus.SUCCESS)
            } catch (ex: Exception) {
                ex.printStackTrace()
                apiCallStatusLiveData.postValue(ApiCallStatus.FAILED)
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
        return queryMap
    }
    private fun reset(){
        pageNumber = 1
        totalPage=1
    }

    fun getMoviesSortByDateHighToLow() {
        if (SortBased.DATE_MAX_TO_MIN == sortBased) {
            return
        }
        sortBased = SortBased.DATE_MAX_TO_MIN
        clearListLiveData.postValue(true)
        reset()
        queryMap["sort_by"] = "release_date.desc"
        getFromServer()
    }

    fun getMoviesSortByDateLowToHigh() {
        if (SortBased.DATE_MIN_TO_MAX == sortBased) {
            return
        }
        sortBased = SortBased.DATE_MIN_TO_MAX
        clearListLiveData.postValue(true)
        reset()
        queryMap["sort_by"] = "release_date.asc"
        getFromServer()
    }

    fun getMoviesSortByRatingLowToHigh() {
        if (SortBased.RATING_LOW_TO__HIGH == sortBased) {
            return
        }
        sortBased = SortBased.RATING_LOW_TO__HIGH
        clearListLiveData.postValue(true)
        reset()
        queryMap["sort_by"] = "vote_average.asc"
        getFromServer()
    }

    fun getMoviesSortByRatingHighToLow() {
        if (SortBased.RATING_HIGH_TO_LOW == sortBased) {
            return
        }
        sortBased = SortBased.RATING_HIGH_TO_LOW
        clearListLiveData.postValue(true)
        reset()
        queryMap["sort_by"] = "vote_average.desc"
        getFromServer()
    }

    fun getMoviesPopularityByHighToLow() {
        if (SortBased.POPULARITY_HIGH_TO_LOW == sortBased) {
            return
        }
        sortBased = SortBased.POPULARITY_HIGH_TO_LOW
        clearListLiveData.postValue(true)
        reset()
        queryMap["sort_by"] = "popularity.desc"
        getFromServer()
    }
    fun getMoviesPopularityByLowToHigh() {
        if (SortBased.POPULARITY_LOW_TO_HIGH == sortBased) {
            return
        }
        sortBased = SortBased.POPULARITY_LOW_TO_HIGH
        clearListLiveData.postValue(true)
        reset()
        queryMap["sort_by"] = "popularity.asc"
        getFromServer()
    }

}
