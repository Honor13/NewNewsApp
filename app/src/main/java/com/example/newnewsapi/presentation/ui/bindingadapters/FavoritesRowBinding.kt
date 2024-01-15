package com.example.newnewsapi.presentation.ui.bindingadapters

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.newnewsapi.R
import com.example.newnewsapi.data.models.FavNews
import com.example.newnewsapi.presentation.viewmodels.MainViewModel

class FavoritesRowBinding {

    companion object {
        @BindingAdapter("loadImageFromUrlFav")
        @JvmStatic
        fun loadingImageFromUrlFav(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)

            }
        }


        @BindingAdapter("applyFavoriteColor")
        @JvmStatic
        fun applyFavoriteColor(imageView: ImageView, favorite: Boolean) {
            if (favorite) {
                imageView.setImageResource(R.drawable.ic_fill_fav)
            }
        }
    }
}