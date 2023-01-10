package com.example.hangman


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman.gameActivity.GameActivity
import com.example.hangman.winLose.LoseActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.button.MaterialButton

class RewardActivity: AppCompatActivity() {

    companion object{
        const val TAG = "REWARED_AD_TAG"
        var mRewardedAd: RewardedAd? = null
    }

    private lateinit var showAdBtn: MaterialButton
    private lateinit var exitBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        MobileAds.initialize(this){initStatus->
            Log.d(TAG,"onCreate: $initStatus")

        }

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("TEST DEVICE ID HERE","TEST DEVICE ID HERE" ))
                .build()
        )
        showAdBtn=findViewById(R.id.showAdBtn)
        exitBtn=findViewById(R.id.ExitBtn)

        showAdBtn.setOnClickListener(){
        showRewaredAd()
        }
        exitBtn.setOnClickListener(){//Hacer que salga de la app

            val intent = Intent(this@RewardActivity, LoseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showRewaredAd() {

        if(mRewardedAd!=null){
            //SE HA CARGADO EL ANUNCIO

            mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(TAG,"onAdClicked: ")
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d(TAG,"onAdDismissedFullScreenContent: ")
                    mRewardedAd=null

                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)

                    Log.d(TAG,"onAdFailedToShowFullScreenContent: ${adError.message}")
                    mRewardedAd = null
                }

                override fun onAdImpression() {
                    super.onAdImpression()

                    Log.d(TAG,"onAdImpression: ")
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()

                    Log.d(TAG,"onAdShowedFullScreenContent: ")
                }
            }

            mRewardedAd!!.show(this){
                Log.d(TAG,"showRewaredAd: ")
                GameActivity.isAdView = true
                finish()

            }

        }
        else{
            Toast.makeText(this,"No se ha cargado aun el anuncio", Toast.LENGTH_SHORT).show()
        }
    }
}