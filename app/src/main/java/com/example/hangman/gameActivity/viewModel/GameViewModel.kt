package com.example.hangman.gameActivity.viewModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hangman.R
import com.example.hangman.gameActivity.ApiHangman
import com.example.hangman.gameActivity.model.GameGuessLetter
import com.example.hangman.gameActivity.model.GameInfo
import com.example.hangman.gameActivity.model.GameSolution
import com.example.hangman.gameActivity.model.GameCheckLetter
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

    val letterSelected = MutableLiveData<GameCheckLetter>()

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

    fun guessLetter(letter : TextView)
    {
        services.guessLetter(gameToken, letter.text[0].toString()).enqueue(object :
            Callback<GameGuessLetter> {
            override fun onResponse(call: Call<GameGuessLetter>, response: Response<GameGuessLetter>)
            {
                letterSelected.postValue(GameCheckLetter(letter, response.body()?.correct ?: false))
                gameWord.postValue(response.body()?.word ?: "")
            }

            override fun onFailure(call: Call<GameGuessLetter>, t: Throwable)
            {
                gameWord.postValue("Error on Api")
            }
        })
    }

}