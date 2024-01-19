package com.example.newnewsapi.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newnewsapi.data.Repository
import com.example.newnewsapi.data.auth.AuthRepository
import com.example.newnewsapi.data.auth.Resource
import com.example.newnewsapi.data.models.Article
import com.example.newnewsapi.data.models.NewsResponse
import com.example.newnewsapi.data.models.Source
import com.example.newnewsapi.util.Constants
import com.example.newnewsapi.util.NetworkResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
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
    var listFav = MutableLiveData<List<Article>>()
    var hidePrgorressBar = MutableLiveData<Boolean>(false)
    val authKey = authRepository.currentUser?.uid
    var favState = MutableLiveData<Boolean>(false)

    // FIREBASE
    // Firebase Firestore
    fun saveFavorites(
        view: View,
        favId: String?,
        auth: String?,
        author: String?,
        content: String?,
        description: String?,
        publishedAt: String?,
        source: Source,
        title: String?,
        url: String?,
        urlToImage: String?
    ) {

        val newFav =
            Article(favId, auth, author, content, description, publishedAt,source,title,url,urlToImage)
        val task = collectionFavorites.document().set(newFav)

        task.addOnSuccessListener {
            Snackbar.make(view, "News added to favorites", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.GREEN).setTextColor(Color.BLACK).show()
            hidePrgorressBar.value = true
        }.addOnFailureListener {
            Snackbar.make(view, "A Problem Has Occured", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.YELLOW).setTextColor(Color.BLACK).show()
            hidePrgorressBar.value = true
        }

    }

    fun checkIfNewsExistsAndSaveFavorites(
        view: View,
        favId: String?,
        auth: String?,
        author: String?,
        content: String?,
        description: String?,
        publishedAt: String?,
        source: Source,
        title: String?,
        url: String?,
        urlToImage: String?
    ) {
        collectionFavorites.whereEqualTo("title", title).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Başlık yoksa haber eklemeye izin ver
                    saveFavorites(
                        view,
                        favId,
                        auth,
                        author,
                        content,
                        description,
                        publishedAt,
                        source,
                        title,
                        url,
                        urlToImage
                    )
                } else {
                    // Başlık varsa kullanıcıyı bilgilendir
                    Snackbar.make(
                        view, "This news is already in favorites", Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(Color.YELLOW).setTextColor(Color.BLACK).show()
                    hidePrgorressBar.value = true
                }
                hidePrgorressBar.value = false
            }.addOnFailureListener {
                // Hata durumunda kullanıcıyı bilgilendir
                Snackbar.make(
                    view, "Error checking for existing news", Snackbar.LENGTH_SHORT
                ).setBackgroundTint(Color.RED).setTextColor(Color.BLACK).show()
                hidePrgorressBar.value = true
            }
        hidePrgorressBar.value = false
    }

    fun checkFavorites(title: String) {
        collectionFavorites.whereEqualTo("title", title).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    favState.value = false
                } else {
                    favState.value = true
                }

            }

    }

    fun loadFavorites(): MutableLiveData<List<Article>> {
        collectionFavorites.addSnapshotListener { value, error ->
            if (value != null) {
                val list = ArrayList<Article>()

                for (d in value.documents) {
                    val fav = d.toObject(Article::class.java)
                    if (fav != null) {
                        fav.id = d.id
                        if (fav.authKey == authKey) list.add(fav)
                    }
                }
                listFav.value = list
            }
        }
        return listFav
    }

    fun deleteFavorites(favId: String, view: View) {

        Snackbar.make(view, "Are you sure you want to delete it?", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.GRAY)
            .setTextColor(Color.BLUE)
            .setActionTextColor(Color.RED)
            .setAction("EVET") {
                val task = collectionFavorites.document(favId)
                task.delete().addOnSuccessListener {
                    Snackbar.make(
                        view, "Deleted", Snackbar.LENGTH_SHORT
                    ).setBackgroundTint(Color.RED).setTextColor(Color.BLACK).show()
                }.addOnFailureListener {
                    Log.e("Dante", "could not be Deleted")
                }
            }.show()


    }

    //////////////////
    //Firebase Authentication

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val loginFlow: LiveData<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val signupFlow: LiveData<Resource<FirebaseUser>?> = _signupFlow

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

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
                val response = repository.remote.getNews(queries)
                newsApiResponse.value = handleNewsResponse(response)

            } catch (e: Exception) {
                newsApiResponse.value = NetworkResult.Error<NewsResponse>("JsonApi Data Not Found")
                val str = NetworkResult.Error<NewsResponse>("JsonApi Data Not Found").data
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