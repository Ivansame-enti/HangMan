package com.example.hangman

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangman.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireBaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        var volume = true
        var vibration = true
        var notification = true
        var advertising = true
        var volumeFlag = true
        var vibrationFlag = true
        var notificationFlag = true
        var advertisingFlag = true
        val currentUser = fireBaseAuth.currentUser?.uid ?: "null"
        supportActionBar?.hide()

        fun saveData(){
            val sharedPref = getSharedPreferences(currentUser, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.apply{
                putBoolean(getString(R.string.SHARED_PREFERENCES_VOLUME),volumeFlag)
                putBoolean(getString(R.string.SHARED_PREFERENCES_VIBRATION),vibrationFlag)
                putBoolean(getString(R.string.SHARED_PREFERENCES_NOTIS),notificationFlag)
                putBoolean(getString(R.string.SHARED_PREFERENCES_ADV),advertisingFlag)
            }.apply()
            val settingsData = hashMapOf(
                getString(R.string.SHARED_PREFERENCES_VOLUME) to volumeFlag, getString(R.string.SHARED_PREFERENCES_VIBRATION) to vibrationFlag, getString(R.string.SHARED_PREFERENCES_NOTIS) to notificationFlag, getString(R.string.SHARED_PREFERENCES_ADV) to advertisingFlag
            )
            firestore.collection(getString(R.string.FIRESTORE_COLLECTION_NAME))
                .document(currentUser)
                .set(settingsData)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Guardado fallado", Toast.LENGTH_SHORT).show()
                }
        }
        fun loadData(){
            val sharedPref = getSharedPreferences(currentUser,Context.MODE_PRIVATE)
            volume = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_VOLUME),true)
            vibration = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_VIBRATION),true)
            notification = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_NOTIS),true)
            advertising = sharedPref.getBoolean(getString(R.string.SHARED_PREFERENCES_ADV),true)
        }
        //BOTONES DE VOLUMEN
        fun volumeController(volumeC: Boolean) {
            volumeFlag = volumeC
            if(volumeFlag){
                binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                binding.offVolume.backgroundTintList = getColorStateList(R.color.OffColorFalse)
            }
            if(!volumeFlag){
                binding.offVolume.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorFalse)
            }
            binding.onVolume.setOnClickListener{
                if(!volumeFlag){
                    binding.offVolume.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                    binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                    volumeFlag = true
                }
            }
            binding.offVolume.setOnClickListener{
                if(volumeFlag){
                    binding.offVolume.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                    binding.onVolume.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                    volumeFlag = false
                }
            }
        }
        //BOTONES DE VIBRACION
        fun vibrationController(vibrationC : Boolean){
            vibrationFlag = vibrationC
            if(vibrationFlag){
                binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                binding.offVibration.backgroundTintList = getColorStateList(R.color.OffColorFalse)
            }
            if(!vibrationFlag){
                binding.offVibration.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorFalse)
            }
            binding.onVibration.setOnClickListener{
                if(!vibrationFlag){
                    binding.offVibration.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                    binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                    vibrationFlag = true
                }
            }
            binding.offVibration.setOnClickListener{
                if(vibrationFlag){
                    binding.offVibration.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                    binding.onVibration.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                    vibrationFlag = false
                }
            }
        }
        //BOTONES DE NOTIFICACION
        fun notificationController(notificationC : Boolean){
            notificationFlag = notificationC
            if(notificationFlag){
                binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                binding.offNotification.backgroundTintList = getColorStateList(R.color.OffColorFalse)
            }
            if(!notificationFlag){
                binding.offNotification.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorFalse)
            }
            binding.onNotification.setOnClickListener{
                if(!notificationFlag){
                    binding.offNotification.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                    binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                    notificationFlag = true
                }
            }
            binding.offNotification.setOnClickListener{
                if(notificationFlag){
                    binding.offNotification.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                    binding.onNotification.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                    notificationFlag = false
                }
            }
        }
        //BOTONES DE ANUNCIOS
        fun advertisingController(advertisingC : Boolean){
            advertisingFlag = advertisingC
            if(advertisingFlag){
                binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                binding.offAdvertising.backgroundTintList = getColorStateList(R.color.OffColorFalse)
            }
            if(!advertisingFlag){
                binding.offAdvertising.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorFalse)
            }
            binding.onAdvertising.setOnClickListener{
                if(!advertisingFlag){
                    binding.offAdvertising.backgroundTintList= getColorStateList(R.color.OffColorFalse)
                    binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorTrue)
                    advertisingFlag = true
                }
            }
            binding.offAdvertising.setOnClickListener{
                if(advertisingFlag) {
                    binding.offAdvertising.backgroundTintList = getColorStateList(R.color.OffColorTrue)
                    binding.onAdvertising.backgroundTintList = getColorStateList(R.color.OnColorFalse)
                    advertisingFlag = false
                }
            }
        }
        fun loadFireStorm(){
            val settingsValors = firestore.collection(getString(R.string.FIRESTORE_COLLECTION_NAME))
                    .document(currentUser)
            settingsValors.get()
                .addOnSuccessListener { result ->
                        volume = result[getString(R.string.SHARED_PREFERENCES_VOLUME)] as Boolean
                        vibration = result[getString(R.string.SHARED_PREFERENCES_VIBRATION)] as Boolean
                        notification = result[getString(R.string.SHARED_PREFERENCES_NOTIS)] as Boolean
                        advertising = (result[getString(R.string.SHARED_PREFERENCES_ADV)] as Boolean)
                    volumeController(volume)
                    vibrationController(vibration)
                    notificationController(notification)
                    advertisingController(advertising)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Consulta fallada", Toast.LENGTH_SHORT).show()
                }
        }
        loadData()
        loadFireStorm()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Aplicamos funciones de botones
        volumeController(volume)
        vibrationController(vibration)
        notificationController(notification)
        advertisingController(advertising)
        binding.backButton.setOnClickListener{
            saveData()
        }
    }
}