package com.example.newnewsapi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newnewsapi.util.Constants.Companion.DEFAULT_CATEGORY_TYPE
import com.example.newnewsapi.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.newnewsapi.util.Constants.Companion.PREFERENCES_CATEGORY
import com.example.newnewsapi.util.Constants.Companion.PREFERENCES_CATEGORY_ID
import com.example.newnewsapi.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject



class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val selectedCategories = stringPreferencesKey(PREFERENCES_CATEGORY)
        val selectedCategoriesId = intPreferencesKey(PREFERENCES_CATEGORY_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    suspend fun saveCategory(categoryType: String, categoryTypeId: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedCategories] = categoryType
            preferences[PreferencesKeys.selectedCategoriesId] = categoryTypeId

        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        context.dataStore.edit { preferences->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    val readBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map {preferences->
            val backOnline = preferences[PreferencesKeys.backOnline] ?: false
            backOnline

        }

    val readCategoryType: kotlinx.coroutines.flow.Flow<CategoryType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedCategoryType =
                preferences[PreferencesKeys.selectedCategories] ?: DEFAULT_CATEGORY_TYPE
            val selectedCategoryTypeId = preferences[PreferencesKeys.selectedCategoriesId] ?: 0

            CategoryType(
                selectedCategoryType,
                selectedCategoryTypeId,

                )
        }
}


data class CategoryType(
    val selectedCategoryType: String,
    val selectedCategoryTypeId: Int,

    )