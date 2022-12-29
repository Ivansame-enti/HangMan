package com.example.hangman

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hangman.gameActivity.GameActivity
import com.example.hangman.databinding.ActivityLoginBinding
import com.example.hangman.LoginRegister.PlayActivity
import com.example.hangman.LoginRegister.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        /*if (fireBaseAuth.currentUser != null) { //Si ya hay un usuario logueado, cambiamos a la otra actividad
            val intent = Intent(this@LoginActivity, PlayActivity::class.java)
            startActivity(intent)
            finish()
        }*/

        binding.loginButton.setOnClickListener{
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty()) {  //Cargamos el juego con el nuevo login
                fireBaseAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener {
                    val intent = Intent(this@LoginActivity, GameActivity::class.java)
                    //createNewUserSettings()
                    startActivity(intent)

                }.addOnFailureListener {
                    Toast.makeText(this, "Wrong username or password ", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerButtonLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.anonLoginButton.setOnClickListener{
            fireBaseAuth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@LoginActivity, GameActivity::class.java)
                        createNewUserSettings()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.userInput.setOnFocusChangeListener { _, b ->
            if(!b){ //Cuando perdemos el focus
                val username = binding.userInput.text.toString()
                if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                    binding.userInput.error = "Invalid username"
                else binding.userInput.error = null
            }
        }
    }

    override fun onResume() { //Cuando volvemos despues de darle a atras
        super.onResume()
        if (fireBaseAuth.currentUser != null) {
            fireBaseAuth = FirebaseAuth.getInstance()
            val intent = Intent(this@LoginActivity, PlayActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun createNewUserSettings(){
        val sharedPref = getSharedPreferences(fireBaseAuth.currentUser?.uid ?: "null", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply(){
            putBoolean("volume",true)
            putBoolean("vibration",true)
            putBoolean("notification",true)
            putBoolean("advertising",true)
        }.apply()
        val settingsData = hashMapOf(
            "volume" to true,
            "vibration" to true,
            "notification" to true,
            "advertising" to true
        )
        firestore.collection("SettingsValue")
            .document(fireBaseAuth.currentUser?.uid ?: "null")
            .set(settingsData)
    }
}