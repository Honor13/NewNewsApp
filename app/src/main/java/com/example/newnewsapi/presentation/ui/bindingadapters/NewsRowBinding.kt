package com.example.newnewsapi.presentation.ui.bindingadapters

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController

import coil.load
import com.example.newnewsapi.R
import com.example.newnewsapi.data.models.Article
import com.example.newnewsapi.data.models.Source
import com.example.newnewsapi.presentation.ui.fragments.bottomnavholder.BottomNavHolderFragmentDirections


class NewsRowBinding {

    companion object {

        @BindingAdapter("onNewsSetOnCLickListener")
        @JvmStatic
        fun onNewsSetOnCLickListener(newsRowLayout: ConstraintLayout, article: Article) {
            newsRowLayout.setOnClickListener {
                try {
                    val safeTitle = article.title ?: "Default Title"
                    val safeContent = article.content ?: "Default Content"
                    val safeAuthor = article.author ?: "Default Author"
                    val safeDescription = article.description ?: "No Description"
                    val safePublishedAt = article.publishedAt ?: "No value Published"
                    val safeSource: Source =
                        article.source ?: Source("no id value", "no name value")
                    val safeUrl = article.url ?: "No Url"
                    val safeUrlToImage = article.urlToImage ?: "No Image Url"

                    val safeArticle = Article(
                        safeAuthor,
                        safeContent,
                        safeDescription,
                        safePublishedAt,
                        safeSource,
                        safeTitle,
                        safeUrl,
                        safeUrlToImage
                    )

                    val action =
                        BottomNavHolderFragmentDirections.actionBottomNavHolderFragmentToDetailsFragment(
                            safeArticle
                        )
                    newsRowLayout.findNavController().navigate(action)
//                    val action = NewsFragmentDirections.actionNewsFragmentToDetailsFragment(safeArticle)
//                        newsRowLayout.findNavController().navigate(action)

                } catch (e: Exception) {
                    Log.e("onNewsSetOnClickListener", e.message.toString())
                }
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