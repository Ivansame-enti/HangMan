package com.example.hangman.gameActivity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.hangman.R
import com.example.hangman.SettingsActivity
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.scores.ScoreActivity
import com.example.hangman.scores.ScoreList
import com.example.hangman.scores.ScoreProvider
import com.example.hangman.winLose.LoseActivity
import com.example.hangman.winLose.WinActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    //Constantes
    private val TIME_TO_NEXT_ACTIVITY: Long = 1000

    //Variables
    private lateinit var timer: CountDownTimer
    private lateinit var wordTextView: TextView
    private lateinit var lettersLayout: ConstraintLayout
    private lateinit var TimerLayout: TextView

    private var volume = true
    private var vibration = true
    private var notification = true
    private var advertising = true

    private var gameWord: String = ""
    private var gameSolution: String = ""
    var gameIntent = 0

    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val gameManager = GameManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Accedemos a elementos del layout
        binding.head.isVisible = false
        binding.body.isVisible = false
        binding.rightArm.isVisible = false
        binding.leftArm.isVisible = false
        binding.rightLeg.isVisible = false
        binding.leftLeg.isVisible = false
        wordTextView = binding.wordTextView
        lettersLayout = binding.lettersLayout
        TimerLayout = binding.TimerLayout
        supportActionBar?.hide()

        //Cuenta atras
        timer = object: CountDownTimer(60000,1){
            override fun onTick(remaining: Long) {

                if(remaining<10000){
                    TimerLayout.text = (remaining/1000).toString()
                    TimerLayout.setTextColor(Color.RED)
                }else{
                    TimerLayout.text = (remaining/1000).toString()
                }
            }

            override fun onFinish() {
                TimerLayout.text =getString(R.string.end_time)
            }
        }

        //Boton de settings
        binding.layout.settingsButton.setOnClickListener{
            val intent = Intent(this@GameActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        //Api hangman
        gameWord = gameManager.startGame() //Empieza la partida
        gameSolution = gameManager.getSolution() //Obtiene la solucion
        showWord() //Muestra la palabra

        //Lee el teclado
        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    if(letterView.text == "A"){
                        letterView.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_right)
                        gameWord = gameSolution
                        checkWin()
                    }
                    else if(gameManager.guessLetter(letterView)){ //La letra esta en la palabra
                        letterView.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_right)
                        gameWord = gameManager.getWord()
                        showWord()
                        checkWin()
                    } else { //La letra no esta en la palabra
                        letterView.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_wrong)
                        gameIntent++
                        checkLose()
                    }
                }
            }
        }

        loadData() //Carga las settings

        //Creamos instancia de firebase
        fireBaseAuth = FirebaseAuth.getInstance()
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        volume = sharedPref.getBoolean("volume",true)
        vibration = sharedPref.getBoolean("vibration",true)
        notification = sharedPref.getBoolean("notification",true)
        advertising = sharedPref.getBoolean("advertising",true)
    }

    private fun showWord(){
        binding.wordTextView.text = gameWord
    }

    override fun onStart() {
        super.onStart()
        timer.start()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private fun checkWin(){
        if(gameWord == gameSolution){
            Handler(Looper.getMainLooper()).postDelayed({
                val email = fireBaseAuth.currentUser?.email ?:"Anonymous"
                val score = 200
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
                    val intent = Intent(this@GameActivity, LoseActivity::class.java)
                    startActivity(intent)
                    finish()
                }, TIME_TO_NEXT_ACTIVITY)
            }
        }
    }
}