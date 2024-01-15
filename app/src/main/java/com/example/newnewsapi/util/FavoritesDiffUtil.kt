package com.example.newnewsapi.util

import androidx.recyclerview.widget.DiffUtil
import com.example.newnewsapi.data.models.Article
import com.example.newnewsapi.data.models.FavNews

class FavoritesDiffUtil(
    private val oldList: List<FavNews>,
    private val newList: List<FavNews>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}