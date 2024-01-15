package com.example.newnewsapi.data.models


import java.io.Serializable

class FavNews(
    var favId: String? = "",
    val authKey: String? = "",
    val author: String? = "",
    val content: String? = "",
    val description: String? = "",
    val publishedAt: String? = "",
    val title: String? = "",
    val url: String? = "",
    val urlToImage: String? = "",
    var favorite: Boolean = false
) : Serializable