package com.example.hangman.gameActivity

import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameManager {

    private lateinit var outside: Retrofit
    private lateinit var services: ApiHangman
    private var gameWord: String = ""
    private var gameToken: String = ""
    private var gameSolution: String = ""
    private var letterInWord: Boolean = false

     fun startGame(): String {
        outside = Retrofit.Builder().baseUrl("http://hangman.enti.cat:5002/").addConverterFactory(
            GsonConverterFactory.create()).build()
        services = outside.create(ApiHangman::class.java)

        services.createGame("en", 6).enqueue(object : Callback<GameInfo> {
            override fun onResponse(call: Call<GameInfo>, response: Response<GameInfo>) {
                gameWord = response.body()?.word ?: "null"
                gameToken = response.body()?.token ?: "null"
            }

            override fun onFailure(call: Call<GameInfo>, t: Throwable) {
                gameWord = "Error on Api"
            }
        })

        return gameWord
    }

    fun getSolution(): String{
        services.getSolution(gameToken).enqueue(object : Callback<GameSolution> {
            override fun onResponse(call: Call<GameSolution>, response: Response<GameSolution>) {
                gameSolution = response.body()?.solution ?: ""
            }

            override fun onFailure(call: Call<GameSolution>, t: Throwable) {
                gameSolution = "Error on Api"
            }
        })
        return gameSolution
    }

    fun guessLetter(letter : TextView): Boolean
    {
        services.guessLetter(gameToken, letter.text[0].toString()).enqueue(object :
            Callback<GameGuessLetter> {
            override fun onResponse(call: Call<GameGuessLetter>, response: Response<GameGuessLetter>)
            {
                letterInWord = response.body()?.correct ?: false
                gameWord = response.body()?.word ?: ""
                gameToken = response.body()?.token ?: ""
            }

            override fun onFailure(call: Call<GameGuessLetter>, t: Throwable)
            {
                gameWord = "Error on Api"
            }
        })
        return letterInWord
    }

    fun getWord(): String{
        return gameWord
    }
}