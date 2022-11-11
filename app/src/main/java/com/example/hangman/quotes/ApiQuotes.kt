package com.example.hangman.quotes

import retrofit2.Call
import retrofit2.http.GET

interface ApiQuotes {

    @GET("random.json/")
    fun getQuote(): Call<Quote>
}