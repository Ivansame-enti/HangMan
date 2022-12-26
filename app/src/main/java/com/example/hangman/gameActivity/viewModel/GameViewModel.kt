package com.example.hangman.gameActivity.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hangman.gameActivity.ApiHangman
import com.example.hangman.gameActivity.GameGuessLetter
import com.example.hangman.gameActivity.GameInfo
import com.example.hangman.gameActivity.GameSolution
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameViewModel : ViewModel() {

    private lateinit var outside: Retrofit
    private lateinit var services: ApiHangman

    val gameWord = MutableLiveData<String>()
    private var gameToken: String = ""

    val gameGuessLetter = MutableLiveData<GameGuessLetter>()
    val gameSolution = MutableLiveData<String>()

    fun startGame() {
        outside = Retrofit.Builder().baseUrl("http://hangman.enti.cat:5002/").addConverterFactory(
            GsonConverterFactory.create()).build()
        services = outside.create(ApiHangman::class.java)

        services.createGame("en", 6).enqueue(object : Callback<GameInfo> {
            override fun onResponse(call: Call<GameInfo>, response: Response<GameInfo>) {
                gameToken = response.body()?.token ?: "null"
                gameWord.postValue(response.body()?.word ?: "null")
                getSolution()
            }

            override fun onFailure(call: Call<GameInfo>, t: Throwable) {
                gameWord.postValue("Error on Api")
                //gameWord = "Error on Api"
            }
        })
    }

    fun getSolution() {
        services.getSolution(gameToken).enqueue(object : Callback<GameSolution> {
            override fun onResponse(call: Call<GameSolution>, response: Response<GameSolution>) {
                gameSolution.postValue(response.body()?.solution ?: "")
            }

            override fun onFailure(call: Call<GameSolution>, t: Throwable) {
                gameSolution.postValue("Error on Api")
            }
        })
    }

}