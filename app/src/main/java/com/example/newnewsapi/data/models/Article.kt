package com.example.newnewsapi.data.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

data class Article(
    @SerializedName("id")
    @Expose
    var id: String? = "",
    @SerializedName("authKey")
    @Expose
    var authKey: String? = "",
    @SerializedName("author")
    @Expose
    var author: String? = "",
    @SerializedName("content")
    @Expose
    val content: String? = "",
    @SerializedName("description")
    @Expose
    val description: String? = "",
    @SerializedName("publishedAt")
    @Expose
    val publishedAt: String? = "",
    @SerializedName("source")
    @Expose
    val source: Source? = null,
    @SerializedName("title")
    @Expose
    val title: String?= "",
    @SerializedName("url")
    @Expose
    val url: String?= "",
    @SerializedName("urlToImage")
    @Expose
    val urlToImage: String?= ""
):Serializable