package com.leanagritest.ui.moviedetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.leanagritest.ApiCallStatus
import com.leanagritest.core.AppObjectController
import com.leanagritest.core.Utils
import com.leanagritest.repository.local.entity.MovieModel
import com.leanagritest.repository.server.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovieDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val _movieDetailMutableLiveData: MutableLiveData<MovieDetail> = MutableLiveData()
    val movieDetailLiveData: LiveData<MovieDetail> = _movieDetailMutableLiveData
    private val queryMap: HashMap<String, String> = HashMap<String, String>()
    val apiCallStatusLiveData: MutableLiveData<ApiCallStatus> = MutableLiveData()

    init {
        queryMap.apply {
            put("language", "en-US")
        }
    }

    fun getMovieDetails(movieModel: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetAvailable()) {
                try {
                    val movieDetailResponse =
                        AppObjectController.moviesNetworkService.getMovieDetails(
                            movieModel.id,
                            getQueryParams(movieModel)
                        )
                    if (movieDetailResponse.isSuccessful) {
                        _movieDetailMutableLiveData.postValue(movieDetailResponse.body())
                    } else if (movieDetailResponse.code() == 404) {
                        apiCallStatusLiveData.postValue(ApiCallStatus.NOT_FOUND)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun getQueryParams(movieId: MovieModel): HashMap<String, String> {
        // queryMap["movie_id"] = movieId.toString()
        return queryMap
    }
}
