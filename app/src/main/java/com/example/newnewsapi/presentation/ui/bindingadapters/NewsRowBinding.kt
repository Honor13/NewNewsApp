package com.example.newnewsapi.presentation.ui.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.newnewsapi.R

class NewsRowBinding {

    companion object{


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadingImageFromUrl(imageView: ImageView, imageUrl: String?){
            imageView.load(imageUrl){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)

            }
        }

    }
}