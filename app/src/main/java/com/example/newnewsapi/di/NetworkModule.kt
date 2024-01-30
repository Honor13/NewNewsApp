package com.example.newnewsapi.di

import com.example.newnewsapi.data.auth.AuthRepository
import com.example.newnewsapi.data.auth.AuthRepositoryImpl
import com.example.newnewsapi.data.network.NewsApi
import com.example.newnewsapi.util.Constants.Companion.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun providesAuthRepository(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }

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