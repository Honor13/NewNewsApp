package com.example.newnewsapi.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newnewsapi.data.models.Article
import com.example.newnewsapi.data.models.NewsResponse
import com.example.newnewsapi.databinding.NewsRowLayoutBinding
import com.example.newnewsapi.util.NewsDiffUtil

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    private var news = emptyList<Article>()

    class MyViewHolder(private val binding: NewsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(article: Article) {
            binding.result = article
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NewsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentNews = news[position]
        holder.bind(currentNews)
    }

    fun setData(newData: NewsResponse) {
        val newsDiffUtil = NewsDiffUtil(news, newData.articles)
        val diffUtilResult = DiffUtil.calculateDiff(newsDiffUtil)
        news = newData.articles
        diffUtilResult.dispatchUpdatesTo(this)
    }
}