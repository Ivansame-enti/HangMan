package com.example.hangman

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.hangman.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fireBaseAuth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireBaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        var volume = true;
        var vibration = true;
        var notification = true;
        var advertising = true;
        var volumeFlag = true;
        var vibrationFlag = true;
        var notificationFlag = true
        var advertisingFlag = true
        var id = 1
        supportActionBar?.hide();

        fun saveData(){
            val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.apply(){
                putBoolean("volume",volumeFlag)
                putBoolean("vibration",vibrationFlag)
                putBoolean("notification",notificationFlag)
                putBoolean("advertising",advertisingFlag)
            }.apply()
            val settingsData = hashMapOf(
                "volume" to volumeFlag,
                "vibration" to vibrationFlag,
                "notification" to notificationFlag,
                "advertising" to advertisingFlag
            )
            firestore.collection("SettingsValue")
                .document(fireBaseAuth.getCurrentUser()?.uid ?: "null")
                .set(settingsData)
                .addOnSuccessListener { result ->
                    Toast.makeText(this, "Guardado exitoso", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Guardado fallado", Toast.LENGTH_SHORT).show()
                }
        }
        fun loadData(){
            val sharedPref = getSharedPreferences("prefs",Context.MODE_PRIVATE)
            volume = sharedPref.getBoolean("volume",true)
            vibration = sharedPref.getBoolean("vibration",true)
            notification = sharedPref.getBoolean("notification",true)
            advertising = sharedPref.getBoolean("advertising",true)
        }
        loadData()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorTrue)
        //binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorTrue)
        //binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorTrue)
        binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorTrue)
        //BOTONES DE VOLUMEN
        volumeFlag = volume
        if(volumeFlag){
            binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorTrue)
            binding.offVolume.backgroundTintList = getColorStateList(R.color.OffColorFalse)
        }else if(!volumeFlag){
            binding.offVolume.backgroundTintList = getColorStateList(R.color.OffColorTrue)
            binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorFalse)
        }
        binding.onVolume.setOnClickListener{
            if(!volumeFlag){
                binding.offVolume.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                volumeFlag = true;
            }
        }
        binding.offVolume.setOnClickListener{
            if(volumeFlag){
                binding.offVolume.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                volumeFlag = false;
            }
        }
        //BOTONES DE VIBRACION
        vibrationFlag = vibration
        if(vibrationFlag){
            binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorTrue)
            binding.offVibration.backgroundTintList = getColorStateList(R.color.OffColorFalse)
        }else if(!vibrationFlag){
            binding.offVibration.backgroundTintList = getColorStateList(R.color.OffColorTrue)
            binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorFalse)
        }
        binding.onVibration.setOnClickListener{
            if(!vibrationFlag){
                binding.offVibration.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                vibrationFlag = true;
            }
        }
        binding.offVibration.setOnClickListener{
            if(vibrationFlag){
                binding.offVibration.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                vibrationFlag = false;
            }
        }
        //BOTONES DE NOTIFICACION
        notificationFlag = notification
        if(notificationFlag){
            binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorTrue)
            binding.offNotification.backgroundTintList = getColorStateList(R.color.OffColorFalse)
        }else if(!notificationFlag){
            binding.offNotification.backgroundTintList = getColorStateList(R.color.OffColorTrue)
            binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorFalse)
        }
        binding.onNotification.setOnClickListener{
            if(!notificationFlag){
                binding.offNotification.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                notificationFlag = true;
            }
        }
        binding.offNotification.setOnClickListener{
            if(notificationFlag){
                binding.offNotification.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                notificationFlag = false;
            }
        }
        //BOTONES DE ANUNCIOS
        advertisingFlag = advertising
        if(advertisingFlag){
            binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorTrue)
            binding.offAdvertising.backgroundTintList = getColorStateList(R.color.OffColorFalse)
        }else if(!advertisingFlag){
            binding.offAdvertising.backgroundTintList = getColorStateList(R.color.OffColorTrue)
            binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorFalse)
        }
        binding.onAdvertising.setOnClickListener{
            if(!advertisingFlag){
                binding.offAdvertising.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                advertisingFlag = true;
            }
        }
        binding.offAdvertising.setOnClickListener{
            if(advertisingFlag) {
                binding.offAdvertising.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                advertisingFlag = false;
            }
        }

        binding.backButton.setOnClickListener{
            saveData()

            //Toast.makeText(this, volume.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}