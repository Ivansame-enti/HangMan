package com.example.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hangman.loginRegister.RegisterActivity
import com.example.hangman.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()

        if (fireBaseAuth.getCurrentUser() != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java);
            startActivity(intent);
            finish();
        }

        binding.loginButton.setOnClickListener{
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()
            if(username.isNotEmpty() && password.isNotEmpty()) {
                fireBaseAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()

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
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.userInput.setOnFocusChangeListener { view, b ->
            if(!b){ //Cuando perdemos el focus
                val username = binding.userInput.text.toString()
                if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                    binding.userInput.error = "Invalid username"
                else binding.userInput.error = null
            }
        }
    }
}