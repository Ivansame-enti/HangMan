package com.example.hangman.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hangman.LoginActivity
import com.example.hangman.MainActivity
import com.example.hangman.databinding.ActivityLoginBinding
import com.example.hangman.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val password2 = binding.confirmPasswordInput.text.toString()

            if(password==password2) {
                if(password.count() >=8) {
                    fireBaseAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = fireBaseAuth.currentUser
                                //updateUI(user)
                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //updateUI(null)
                            }
                        }
                }else Toast.makeText(this, "Passwords need at least 8 characters", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
        }

        binding.goBackButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
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