package com.example.newnewsapi.data

import com.example.newnewsapi.data.models.NewsResponse
import com.example.newnewsapi.data.network.NewsApi
import com.google.firebase.firestore.CollectionReference
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val newsApi: NewsApi,

) {

    suspend fun getNews(queries: Map<String, String>):Response<NewsResponse> {
        return newsApi.getNews(queries)
    }


}