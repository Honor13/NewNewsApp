package com.example.newnewsapi.data.network

import com.example.newnewsapi.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsApi {

    @GET("top-headlines")
    suspend fun getNews(
        @QueryMap queries: Map<String, String>
    ): Response<NewsResponse>
}