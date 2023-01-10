package com.example.hangman.LoginRegister

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hangman.LoginActivity
import com.example.hangman.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.registerButton.setOnClickListener {
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val password2 = binding.confirmPasswordInput.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                if (password == password2) {
                    if (password.count() >= 8) {
                        fireBaseAuth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    createNewUserSettings()
                                    startActivity(intent)
                                }
                                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()
                            }
                    } else Toast.makeText(
                        this,
                        "Passwords need at least 8 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.userInput.setOnFocusChangeListener { _, focus ->
            if(!focus){
                val username = binding.userInput.text.toString()
                if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                    binding.userInput.error = "Invalid username"
                else binding.userInput.error = null
            }
        }

    }
    private fun createNewUserSettings(){
        val sharedPref = getSharedPreferences(fireBaseAuth.currentUser?.uid ?: "null", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply{
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
            .addOnSuccessListener { _ ->
                Toast.makeText(this, "Guardado exitoso", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { _ ->
                Toast.makeText(this, "Guardado fallado", Toast.LENGTH_SHORT).show()
            }
    }
}