package com.example.newnewsapi.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.newnewsapi.R
import com.example.newnewsapi.data.Repository
import com.example.newnewsapi.data.auth.AuthRepository
import com.example.newnewsapi.data.auth.Resource
import com.example.newnewsapi.data.models.FavNews
import com.example.newnewsapi.data.models.NewsResponse
import com.example.newnewsapi.util.Constants
import com.example.newnewsapi.util.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val collectionFavorites: CollectionReference,
    private val authRepository: AuthRepository,
    application: Application
) : AndroidViewModel(application) {
    var newsApiResponse: MutableLiveData<NetworkResult<NewsResponse>> = MutableLiveData()

    // FIREBASE
    // Firebase Firestore
    fun saveFavorites(
        favId: String?, auth: String?, author: String?, content: String?, description: String?,
        publishedAt: String?, title: String?, url: String?, urlToImage: String?
    ) {

        val newFav =
            FavNews(favId, auth, author, content, description, publishedAt, title, url, urlToImage)
        collectionFavorites.document().set(newFav)
    }
    //////////////////
    //Firebase Authentication

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val loginFlow: LiveData<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val signupFlow: LiveData<Resource<FirebaseUser>?> = _signupFlow

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

//    init {
//        if (authRepository.currentUser != null) {
//            _loginFlow.value = Resource.Success(authRepository.currentUser!!)
//        }
//    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = authRepository.login(email, password)
        _loginFlow.value = result
    }

    fun signupUser(name: String, email: String, password: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = authRepository.signup(name, email, password)
        _signupFlow.value = result
    }

    fun logout() {
        authRepository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }
    ////////////////////////
    //////////////////////////////////////
    fun getNews(queries: Map<String, String>) = viewModelScope.launch {
        getNewsSafeCall(queries)
    }

    private suspend fun getNewsSafeCall(queries: Map<String, String>) {

        if (hasInternetConnection()) {
            try {
                var response = repository.remote.getNews(queries)
                newsApiResponse.value = handleNewsResponse(response)

            } catch (e: Exception) {
                newsApiResponse.value = NetworkResult.Error<NewsResponse>("JsonApi Data Not Found")
                var str = NetworkResult.Error<NewsResponse>("JsonApi Data Not Found").data
                Log.e("Dante", str.toString())
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

            response.code() == 500 -> {
                return NetworkResult.Error("Server Error.")
            }

            response.code() == 429 -> {
                return NetworkResult.Error("Set query limit exceeded.")
            }

            response.body()?.articles.isNullOrEmpty() -> {
                return NetworkResult.Error("Not Found.")
            }

            response.isSuccessful -> {
                val jsonApiData = response.body()
                return NetworkResult.Success(jsonApiData!!)
            }

            else -> {
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

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_COUNTRY] = Constants.COUNTRY
        queries[Constants.QUERY_CATEGORY] = Constants.DEFAULT_CATEGORY_TYPE
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY

        return queries
    }


}