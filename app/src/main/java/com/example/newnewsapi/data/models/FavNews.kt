package com.example.newnewsapi.data.models



import java.io.Serializable

class FavNews (
    val favId: String? ="",
    val author: String? ="",
    val content: String? ="",
    val description: String? ="",
    val publishedAt: String? ="",
    val title: String? ="",
    val url: String? ="",
    val urlToImage: String? =""
):Serializable