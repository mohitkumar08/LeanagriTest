package com.leanagritest.ui.movielist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leanagritest.databinding.MoviesListItemViewBinding
import com.leanagritest.repository.local.entity.MovieModel

class MoviesListAdapter(private val movieList: ArrayList<MovieModel> = arrayListOf()) :
    RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MoviesListItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size


    inner class ViewHolder(private val binding: MoviesListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(certificationQuestion: MovieModel) {
            with(binding) {

            }
        }
    }
}
