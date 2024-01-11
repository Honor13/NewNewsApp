package com.example.newnewsapi.data.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Source(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String
)