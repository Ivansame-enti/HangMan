package com.example.hangman.nasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNasa {
    @GET("search/")
    fun preformSearch(@Query("q") query: String): Call<ImagesList>
}