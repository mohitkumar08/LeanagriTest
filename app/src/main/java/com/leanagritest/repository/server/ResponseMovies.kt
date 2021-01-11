package com.leanagritest.repository.server


import com.google.gson.annotations.SerializedName
import com.leanagritest.repository.local.entity.MovieModel

data class ResponseMovies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)