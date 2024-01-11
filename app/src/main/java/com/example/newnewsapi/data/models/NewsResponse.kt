package com.example.newnewsapi.data.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class NewsResponse(
    @SerializedName("articles")
    @Expose
    val articles: List<Article>,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("totalResults")
    @Expose
    val totalResults: Int
)