package com.example.newnewsapi.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newnewsapi.data.models.Article
import com.example.newnewsapi.databinding.FavoritesRowLayoutBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.example.newnewsapi.util.NewsDiffUtil


class FavoritesAdapter(private val onItemClickListener: (Article) -> Unit) :
    RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>() {

    private var favNews = emptyList<Article>()

     class MyViewHolder(private val binding: FavoritesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article,onItemClickListener: (Article) -> Unit) {
            binding.article = article

            binding.imageView33.setOnClickListener {
//                mainViewModel.deleteFavorites(article, binding.imageView33)
                onItemClickListener.invoke(article)
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
        holder.bind(currentfavNews, onItemClickListener)
    }

    fun setData(newData: List<Article>) {
        val favDiffUtil = NewsDiffUtil(favNews, newData)
        val diffUtilResult = DiffUtil.calculateDiff(favDiffUtil)
        favNews = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}