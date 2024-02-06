package com.example.newnewsapi.di

import android.content.Context
import android.content.SharedPreferences
import com.example.auth.data.repository.AuthRepository
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.newnewsapi.data.network.NewsApi
import com.example.newnewsapi.util.Constants
import com.example.newnewsapi.util.Constants.Companion.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // FIREBASE DEPENDENCYS
    // Firestore
    @Singleton
    @Provides
    fun provideCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("Favorites")
    }
    ////////////////////////////////////////

    // Firebase Authentication

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        appPref: SharedPreferences

    ): AuthRepository {
        return AuthRepositoryImpl(auth,appPref)
    }

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Constants.SHARED_PREF_FILE_NAME,
            Context.MODE_PRIVATE)
    }



//    @Provides
//    fun providesSharedPref(@ApplicationContext context: Context): SharedPreferences {
//        return context.getSharedPreferences(
//            Constants.SHARED_PREF_FILE_NAME,
//            Context.MODE_PRIVATE
//        )
//    }

    ///////////////////////////////////////
/////////////////////////////////////////////////////////
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }
}