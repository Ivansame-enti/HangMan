package com.example.hangman.gameActivity

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiHangman {
    @POST("hangman")
    fun createGame(@Query("lang") lang : String, @Query("maxTries") maxTries : Int) : Call<GameInfo>

    @PUT("hangman")
    fun guessLetter(@Query("token") token : String, @Query("letter") letter : String) : Call<GameGuessLetter>

    @GET("hangman")
    fun getSolution(@Query("token") token : String) : Call<GameSolution>
}