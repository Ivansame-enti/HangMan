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
    private val FIRESTORE_COLLECTION_NAME = "SettingsValue"
    private val SHARED_PREFERENCES_VOLUME = "volume"
    private val SHARED_PREFERENCES_VIBRATION = "vibration"
    private val SHARED_PREFERENCES_NOTIS = "notification"
    private val SHARED_PREFERENCES_ADV = "advertising"

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
                putBoolean(SHARED_PREFERENCES_VOLUME,volumeFlag)
                putBoolean(SHARED_PREFERENCES_VIBRATION,vibrationFlag)
                putBoolean(SHARED_PREFERENCES_NOTIS,notificationFlag)
                putBoolean(SHARED_PREFERENCES_ADV,advertisingFlag)
            }.apply()
            val settingsData = hashMapOf(
                SHARED_PREFERENCES_VOLUME to volumeFlag, SHARED_PREFERENCES_VIBRATION to vibrationFlag, SHARED_PREFERENCES_NOTIS to notificationFlag, SHARED_PREFERENCES_ADV to advertisingFlag
            )
            firestore.collection(FIRESTORE_COLLECTION_NAME)
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
            volume = sharedPref.getBoolean(SHARED_PREFERENCES_VOLUME,true)
            vibration = sharedPref.getBoolean(SHARED_PREFERENCES_VIBRATION,true)
            notification = sharedPref.getBoolean(SHARED_PREFERENCES_NOTIS,true)
            advertising = sharedPref.getBoolean(SHARED_PREFERENCES_ADV,true)
            Toast.makeText(this, notification.toString(), Toast.LENGTH_SHORT).show()
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
            val settingsValors = firestore.collection(FIRESTORE_COLLECTION_NAME)
                    .document(currentUser)
            settingsValors.get()
                .addOnSuccessListener { result ->
                        volume = result[SHARED_PREFERENCES_VOLUME] as Boolean
                        vibration = result[SHARED_PREFERENCES_VIBRATION] as Boolean
                        notification = result[SHARED_PREFERENCES_NOTIS] as Boolean
                        advertising = (result[SHARED_PREFERENCES_ADV] as Boolean)
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