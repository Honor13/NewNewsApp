package com.example.newnewsapi.presentation.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newnewsapi.data.CategoryType
import com.example.newnewsapi.data.DataStoreRepository
import com.example.newnewsapi.util.Constants
import com.example.newnewsapi.util.Constants.Companion.DEFAULT_CATEGORY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var categoryType = DEFAULT_CATEGORY_TYPE

    var networkStatus = false
    var backOnline = false

    val readCategoryType = dataStoreRepository.readCategoryType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveCategory(categoryType: String, categoryTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveCategory(categoryType, categoryTypeId)
        }

    fun saveBackOnline(backOnline: Boolean)=
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readCategoryType.collect { value ->
                categoryType = value.selectedCategoryType

            }
        }

        queries[Constants.QUERY_COUNTRY] = Constants.COUNTRY
        queries[Constants.QUERY_CATEGORY] = categoryType
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY

        return queries
    }

    fun showNetworkStatus(){
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection",Toast.LENGTH_LONG).show()
            saveBackOnline(true)
        } else if (networkStatus){
            if (backOnline){
                Toast.makeText(getApplication(), "We are back online",Toast.LENGTH_LONG).show()
                saveBackOnline(false)
            }
        }
    }
}