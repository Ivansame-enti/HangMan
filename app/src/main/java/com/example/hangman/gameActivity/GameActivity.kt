package com.example.hangman.gameActivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.hangman.R
import com.example.hangman.RewardActivity
import com.example.hangman.SettingsActivity
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.gameActivity.viewModel.GameViewModel
import com.example.hangman.scores.ScoreList
import com.example.hangman.scores.ScoreProvider
import com.example.hangman.winLose.LoseActivity
import com.example.hangman.winLose.WinActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var database: DatabaseReference

    private val TIME_TO_NEXT_ACTIVITY: Long = 500

    private lateinit var timer: CountDownTimer
    private var timerActualValue: Long = 60
    private lateinit var wordTextView: TextView
    private lateinit var lettersLayout: ConstraintLayout
    private lateinit var timerLayout: TextView
    private var volume = true
    private var vibration = true
    private var notification = true
    private var advertising = true

    private var gameWord: String = ""
    private var gameSolution: String = ""
    private var gameIntent = 0

    private lateinit var fireBaseAuth: FirebaseAuth
    private val gameViewModel : GameViewModel by viewModels()
    private val notificationId= 101
    private var mMediaPlayer: MediaPlayer? = null
    private var ticSound: MediaPlayer? = null
    private lateinit var currentUser : String

    companion object{
        var isAdView = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        wordTextView = binding.wordTextView
        lettersLayout = binding.lettersLayout
        timerLayout = binding.TimerLayout
        supportActionBar?.hide()
        isAdView = false
        //Boton de settings
        binding.layout.settingsButton.setOnClickListener{
            val intent = Intent(this@GameActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        //Usamos el viewModel
        gameViewModel.startGame()
        gameViewModel.gameWord.observe(this, Observer {
            gameWord = it.toString()
            showWord() //Muestra la palabra
            checkWin()
        })

        gameViewModel.gameSolution.observe(this, Observer {
            gameSolution = it.toString()
            Toast.makeText(this, gameSolution, Toast.LENGTH_SHORT).show()
        })

        gameViewModel.letterSelected.observe(this, Observer {
            if(it.correct){
                it.letter.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_right)
            }
            else{
                it.letter.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_wrong)
                gameIntent++
                checkLose()
            }
        })

        //Lee el teclado
        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    gameViewModel.guessLetter(letterView)
                    val sharedPref = getSharedPreferences(currentUser,Context.MODE_PRIVATE)
                    volume = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_VOLUME),true)
                    if(volume){
                        ticSound = MediaPlayer.create(this, R.raw.click)
                        ticSound!!.start()
                    }
                }
            }
        }

        createNotificationChannel()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        fireBaseAuth = FirebaseAuth.getInstance()
        currentUser = fireBaseAuth.currentUser?.uid ?: "null"
        loadData()
        loadRewardAd()

        if(isAdView){
            gameIntent = 0
             binding.head.isVisible = false
             binding.body.isVisible = false
             binding.rightArm.isVisible = false
             binding.leftArm.isVisible = false
             binding.rightLeg.isVisible = false
             binding.leftLeg.isVisible = false
             isAdView=false
        }
        //Activamos el  timer
        timer = object: CountDownTimer(timerActualValue*1000,1){
            override fun onTick(remaining: Long) {
                timerActualValue = remaining/1000
                timerLayout.text = timerActualValue.toString()
                if(timerActualValue<10) timerLayout.setTextColor(Color.RED)
            }

            override fun onFinish() {
                timerLayout.text =getString(R.string.end_time)
                gameIntent=6
                checkLose()
            }
        }.start()
        if(gameIntent==6){
            val intent = Intent(this@GameActivity, LoseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createNotificationChannel(){
        val name = "Notification Title "
        val descriptionText = "Notification description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(getString(R.string.CHANNEL_ID),name,importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this,getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("You win!")
            .setContentText("God job, keep playing like that")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }

    private fun showWord(){
        binding.wordTextView.text = gameWord
    }

    private fun checkWin(){
        if(gameWord == gameSolution){
            mMediaPlayer?.pause()
            val email = fireBaseAuth.currentUser?.email ?:"Anonymous"
            var username:String
            if(email.isEmpty()){
                username = "Anonymous"
            }else{
                val index = email.indexOf('@')
                username = email.substring(0,index)
            }
            val score = timerActualValue.toInt() * gameWord.count()
            database = FirebaseDatabase.getInstance().getReference("Players")
            val User = User(email,score)
            database.child(username).setValue(User)

            ScoreProvider.GetData()

            Handler(Looper.getMainLooper()).postDelayed({

               // if(email.isNotEmpty()) ScoreProvider.scoreListDef+= ScoreList(fireBaseAuth.currentUser?.email ?:"Anonymous", "score:$score") //Añadimos el jugador a la ScoreList

               // else ScoreProvider.scoreListDef+= ScoreList("Anonymous", "score:$score") //Añadimos el jugador a la ScoreList
                val intent = Intent(this@GameActivity, WinActivity::class.java)
                intent.putExtra("score", score)


                if(notification){
                    sendNotification()
                }
                loadData()
                startActivity(intent)
                finish()
            }, TIME_TO_NEXT_ACTIVITY)
        }
    }

    private fun checkLose(){
        when (gameIntent) {
            1 -> binding.head.isVisible = true
            2 -> binding.body.isVisible = true
            3 -> binding.rightArm.isVisible = true
            4 -> binding.leftArm.isVisible = true
            5 -> binding.rightLeg.isVisible = true
            6 -> {
                binding.leftLeg.isVisible = true
                    mMediaPlayer?.pause()
                val intent = Intent(this@GameActivity, RewardActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences(currentUser,Context.MODE_PRIVATE)
        volume = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_VOLUME),true)
        vibration = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_VIBRATION),true)
        notification = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_NOTIS),true)
        advertising = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_ADV),true)
        if(!volume){
            if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
        }else if(volume){
            mMediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        }
    }

    private fun loadRewardAd(){
        RewardedAd.load(
            this,
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
}