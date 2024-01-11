package com.example.newnewsapi.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newnewsapi.data.Repository
import com.example.newnewsapi.data.models.NewsResponse
import com.example.newnewsapi.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
):AndroidViewModel(application)
{
    var newsApiResponse: MutableLiveData<NetworkResult<NewsResponse>> = MutableLiveData()


    fun getNews(queries: Map<String, String>) = viewModelScope.launch {
        getNewsSafeCall(queries)
    }

    private suspend fun getNewsSafeCall(queries: Map<String, String>) {

        if (hasInternetConnection()){
            try {
                var response = repository.remote.getNews(queries)
                newsApiResponse.value = handleNewsResponse(response)

            }catch (e:Exception){
                newsApiResponse.value = NetworkResult.Error<NewsResponse>("JsonApi Data Not Found")
                var str = NetworkResult.Error<NewsResponse>("JsonApi Data Not Found").data
                Log.e("Dante",str.toString())
            }

        } else {
            newsApiResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleNewsResponse(response: Response<NewsResponse>): NetworkResult<NewsResponse>? {
        newsApiResponse.value = NetworkResult.Loading()
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout.")
            }

            response.code() == 400 -> {
                return NetworkResult.Error("Bad Request.")
            }

            response.code() == 500 ->{
                return NetworkResult.Error("Server Error.")
            }

            response.code() == 429 ->{
                return NetworkResult.Error("Set query limit exceeded.")
            }
            response.body()?.articles.isNullOrEmpty() -> {
                return NetworkResult.Error("Not Found.")
            }
            response.isSuccessful -> {
                val jsonApiData = response.body()
                return NetworkResult.Success(jsonApiData!!)
            }
            else ->{
                return NetworkResult.Error(response.message())
            }

        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }
}