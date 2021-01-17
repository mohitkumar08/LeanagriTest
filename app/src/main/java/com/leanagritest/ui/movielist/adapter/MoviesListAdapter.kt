package com.leanagritest.ui.movielist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leanagritest.R
import com.leanagritest.core.Utils.setRoundImage
import com.leanagritest.databinding.MoviesListItemViewBinding
import com.leanagritest.repository.local.entity.MovieModel


class MoviesListAdapter(
    private val movieList: ArrayList<MovieModel> = arrayListOf(),
    private val listener: OnMoviesSelectListener? = null
) :
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

    fun addMovies(mNewMovieList: List<MovieModel>) {
        //  val diffCallback = MoviesDiffCallback(movieList, mNewMovieList)
        // val diffResult = DiffUtil.calculateDiff(diffCallback)
        //  movieList.clear()
        val oldSize = movieList.size
        movieList.addAll(mNewMovieList)
        notifyItemRangeInserted(oldSize, mNewMovieList.size)
        //     notifyDataSetChanged()
        //     diffResult.dispatchUpdatesTo(this)
    }

    fun clear() {
        movieList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: MoviesListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(moviesModel: MovieModel) {
            with(binding) {
                tvMovieName.text = moviesModel.originalTitle
                tvRating.text = moviesModel.rating.toString()
                tvYear.text = moviesModel.releaseDate.split("-")[0]
                imageView.setImageResource(R.drawable.img_placeholder)
                if (moviesModel.posterPath.isNullOrEmpty().not()) {
                    imageView.setRoundImage(moviesModel.getModifiedPath())
                }
                root.setOnClickListener {
                    listener?.onClickMovie(moviesModel)
                }
            }
        }
    }
}

interface OnMoviesSelectListener {
    fun onClickMovie(movie: MovieModel)
}
