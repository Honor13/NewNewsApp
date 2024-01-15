package com.example.newnewsapi.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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


    val readCategoryType = dataStoreRepository.readCategoryType

    fun saveCategory(categoryType: String, categoryTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveCategory(categoryType, categoryTypeId)
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
}