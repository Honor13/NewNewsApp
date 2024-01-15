package com.example.newnewsapi.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newnewsapi.data.models.FavNews
import com.example.newnewsapi.databinding.FavoritesRowLayoutBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.example.newnewsapi.util.FavoritesDiffUtil


class FavoritesAdapter(var mainViewModel: MainViewModel) :
    RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>() {

    private var favNews = emptyList<FavNews>()

    class MyViewHolder(private val binding: FavoritesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: FavNews, mainViewModel: MainViewModel) {
            binding.favs = article

            binding.imageView33.setOnClickListener {
                mainViewModel.deleteFavorites(article.favId.toString(), binding.imageView33)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return favNews.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentfavNews = favNews[position]
        holder.bind(currentfavNews, mainViewModel)
    }

    fun setData(newData: List<FavNews>) {
        val favDiffUtil = FavoritesDiffUtil(favNews, newData)
        val diffUtilResult = DiffUtil.calculateDiff(favDiffUtil)
        favNews = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}