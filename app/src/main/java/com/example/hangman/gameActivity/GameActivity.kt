package com.example.hangman.gameActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.hangman.R
import com.example.hangman.SettingsActivity
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.scores.ScoreActivity
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    //Constantes
    private val TIME_TO_NEXT_ACTIVITY: Long = 2000

    private lateinit var wordTextView: TextView
    private lateinit var lettersLayout: ConstraintLayout

    private lateinit var outside: Retrofit
    private lateinit var services: ApiHangman

    private var volume = true;
    private var vibration = true;
    private var notification = true;
    private var advertising = true;

    private var gameToken: String = ""
    private var gameWord: String = ""
    private var gameSolution: String = ""
    var gameIntent = 0;

    private lateinit var firestore: FirebaseFirestore

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

        supportActionBar?.hide();

        //Boton de settings
        binding.layout.settingsButton.setOnClickListener{
            val intent = Intent(this@GameActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        //Lee el teclado
        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    guessLetter(letterView)
                }
            }
        }

        //Variables para API hangman
        outside = Retrofit.Builder().baseUrl("https://hangman-api.herokuapp.com/").addConverterFactory(GsonConverterFactory.create()).build()
        services = outside.create(ApiHangman::class.java)

        loadData() //Carga las settings

        startGame() //Empieza la partida
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

    private fun startGame(){
        services.createGame().enqueue(object : Callback<GameInfo> {
            override fun onResponse(call: Call<GameInfo>, response: Response<GameInfo>) {
                gameWord = response.body()?.word ?: ""
                gameToken = response.body()?.token ?: ""
                showWord()
                getWord()
            }

            override fun onFailure(call: Call<GameInfo>, t: Throwable) {
                Toast.makeText(this@GameActivity, "Error on API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWord(){
        services.getSolution(gameToken).enqueue(object : Callback<GameSolution> {
            override fun onResponse(call: Call<GameSolution>, response: Response<GameSolution>) {
                gameSolution = response.body()?.solution ?: ""
                Toast.makeText(this@GameActivity, gameSolution, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<GameSolution>, t: Throwable) {
                Toast.makeText(this@GameActivity, "Error at getting solution", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guessLetter(letter : TextView)
    {
        services.guessLetter(gameToken, letter.text[0].toString()).enqueue(object : Callback<GameGuessLetter>{
            override fun onResponse(call: Call<GameGuessLetter>,response: Response<GameGuessLetter>)
            {
                val letterInWord : Boolean = response.body()?.correct ?: false
                gameWord = response.body()?.word ?: "";
                gameToken = response.body()?.token ?: ""

                if(letterInWord) {
                    letter.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_right)
                    showWord()
                    checkWin()
                } else {
                    letter.background = ContextCompat.getDrawable(this@GameActivity, R.drawable.letters_background_wrong)
                    gameIntent++
                    checkLose()
                }
            }

            override fun onFailure(call: Call<GameGuessLetter>, t: Throwable)
            {
                Toast.makeText(this@GameActivity, "Error on API", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun checkWin(){
        if(gameWord == gameSolution){
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@GameActivity, ScoreActivity::class.java)
                startActivity(intent)
            }, TIME_TO_NEXT_ACTIVITY)
        }
    }

    private fun checkLose(){
        when (gameIntent) {
            1 -> binding.head.isVisible = true;
            2 -> binding.body.isVisible = true;
            3 -> binding.rightArm.isVisible = true
            4 -> binding.leftArm.isVisible = true
            5 -> binding.rightLeg.isVisible = true
            6 -> {
                binding.leftLeg.isVisible = true
                gameWord = gameSolution
                showWord()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@GameActivity, ScoreActivity::class.java)
                    startActivity(intent)
                }, TIME_TO_NEXT_ACTIVITY)
            }
        }
    }
}