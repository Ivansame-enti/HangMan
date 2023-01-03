package com.example.hangman.gameActivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
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
import com.example.hangman.SettingsActivity
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.gameActivity.viewModel.GameViewModel
import com.example.hangman.scores.ScoreList
import com.example.hangman.scores.ScoreProvider
import com.example.hangman.winLose.LoseActivity
import com.example.hangman.winLose.WinActivity
import com.google.firebase.auth.FirebaseAuth

class GameActivity : AppCompatActivity() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId= 101
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var binding: ActivityGameBinding

    //Constantes
    private val TIME_TO_NEXT_ACTIVITY: Long = 500

    //Variables
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordTextView = binding.wordTextView
        lettersLayout = binding.lettersLayout
        timerLayout = binding.TimerLayout
        supportActionBar?.hide()

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
            Toast.makeText(
                this,
                gameSolution,
                Toast.LENGTH_SHORT
            ).show()
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
                }
            }
        }
        fun loadData(){
            val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            volume = sharedPref.getBoolean("volume",true)
            vibration = sharedPref.getBoolean("vibration",true)
            notification = sharedPref.getBoolean("notification",true)
            advertising = sharedPref.getBoolean("advertising",true)
        }
        //loadData() //Carga las settings
        createNotificationChannel()
        fireBaseAuth = FirebaseAuth.getInstance() //Creamos instancia de firebase

        var mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+this.packageName+"/"+ R.raw.backgroundmusic))
        mediaPlayer.prepare()
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title "
            val descriptionText = "Notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("You win!")
            .setContentText("Play more pls")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }

    private fun showWord(){
        binding.wordTextView.text = gameWord
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onResume() {
        super.onResume()

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
    }

    private fun checkWin(){
        if(gameWord == gameSolution){
            Handler(Looper.getMainLooper()).postDelayed({
                val email = fireBaseAuth.currentUser?.email ?:"Anonymous"
                val score = timerActualValue.toInt() * gameWord.count()
                if(email.isNotEmpty()) ScoreProvider.scoreListDef+= ScoreList(fireBaseAuth.currentUser?.email ?:"Anonymous", score) //Añadimos el jugador a la ScoreList
                else ScoreProvider.scoreListDef+= ScoreList("Anonymous", score) //Añadimos el jugador a la ScoreList
                val intent = Intent(this@GameActivity, WinActivity::class.java)
                intent.putExtra("score", score)
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
                gameWord = gameSolution
                showWord()
                Handler(Looper.getMainLooper()).postDelayed({
                    if(notification){
                        sendNotification()
                    }
                    val intent = Intent(this@GameActivity, LoseActivity::class.java)
                    startActivity(intent)
                    finish()
                }, TIME_TO_NEXT_ACTIVITY)
            }
        }
    }
}