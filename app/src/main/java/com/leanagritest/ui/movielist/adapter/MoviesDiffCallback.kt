package com.leanagritest.ui.movielist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.leanagritest.repository.local.entity.MovieModel

class MoviesDiffCallback(
    private val mOldMovieModelList: List<MovieModel>,
    private val mNewMovieModelList: List<MovieModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldMovieModelList.size
    }


    override fun getNewListSize(): Int {
        return mNewMovieModelList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldMovieModelList[oldItemPosition].id == mNewMovieModelList[newItemPosition].id
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie = mOldMovieModelList[oldItemPosition]
        val newMovie = mNewMovieModelList[newItemPosition]
        return oldMovie.originalTitle == newMovie.originalTitle
    }

}