package com.example.hangman.GameActivity

import retrofit2.Call
import retrofit2.http.POST

interface ApiHangman {
    @POST("hangman")
    fun createGame() : Call<GameInfo>

    /*@PUT("hangman/")
    fun guessLetter()*/
}