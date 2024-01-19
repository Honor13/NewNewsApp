package com.example.newnewsapi.presentation.ui.bindingadapters


import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.newnewsapi.R
import com.example.newnewsapi.data.models.Article
import com.example.newnewsapi.presentation.ui.fragments.bottomnavholder.BottomNavHolderFragmentDirections



class NewsRowBinding {

    companion object {

        @BindingAdapter("onNewsSetOnCLickListener")
        @JvmStatic
        fun onNewsSetOnCLickListener(newsRowLayout: ConstraintLayout, article: Article) {
            newsRowLayout.setOnClickListener {

                    val action =
                        BottomNavHolderFragmentDirections.actionBottomNavHolderFragmentToDetailsFragment(
                            article
                        )
                    newsRowLayout.findNavController().navigate(action)

            }
        }


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadingImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)

            }
        }

    }
}