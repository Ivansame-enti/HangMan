package com.example.hangman.gameActivity.viewModel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.hangman.R
import com.example.hangman.RewardActivity
import com.example.hangman.gameActivity.ApiHangman
import com.example.hangman.gameActivity.model.GameGuessLetter
import com.example.hangman.gameActivity.model.GameInfo
import com.example.hangman.gameActivity.model.GameSolution
import com.example.hangman.gameActivity.model.GameCheckLetter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var outside: Retrofit
    private lateinit var services: ApiHangman

    val context = getApplication<Application>().applicationContext
    private val notificationId= 101

    val gameWord = MutableLiveData<String>()
    private var gameToken: String = ""
    val letterSelected = MutableLiveData<GameCheckLetter>()
    val gameSolution = MutableLiveData<String>()

    fun startGame() {
        outside = Retrofit.Builder().baseUrl("http://hangman.enti.cat:5002/").addConverterFactory(
            GsonConverterFactory.create()).build()
        services = outside.create(ApiHangman::class.java)
        MobileAds.initialize(context) {}
        services.createGame("en", 50).enqueue(object : Callback<GameInfo> {
            override fun onResponse(call: Call<GameInfo>, response: Response<GameInfo>) {
                gameToken = response.body()?.token ?: "null"
                gameWord.postValue(response.body()?.word ?: "null")
                getSolution()

                //LANZA EVENTO CADA VEZ QUE INICIA PARTIDA
                //val context = getApplication<Application>().applicationContext
                val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                val bundle = Bundle()
                bundle.putString("Message", "Partida Iniciada")
                analytics.logEvent("level_start",bundle)
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

    fun loadRewardAd(){
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object: RewardedAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    RewardActivity.mRewardedAd =null
                }

                override fun onAdLoaded(RewardedAd: RewardedAd) {
                    super.onAdLoaded(RewardedAd)


                    RewardActivity.mRewardedAd = RewardedAd
                }
            }
        )
    }

    fun createNotificationChannel(){
        val name = "Notification Title "
        val descriptionText = "Notification description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id_example_01",name,importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun sendNotification(){
        val builder = NotificationCompat.Builder(context,"channel_id_example_01")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("You win!")
            .setContentText("God job, keep playing like that")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)){
            notify(notificationId,builder.build())
        }
    }

}