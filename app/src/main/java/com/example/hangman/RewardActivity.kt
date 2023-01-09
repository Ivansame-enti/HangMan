package com.example.hangman


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.button.MaterialButton

class RewardActivity: AppCompatActivity() {


    private companion object{
        const val TAG = "REWARED_AD_TAG"
    }

    private lateinit var showAdBtn: MaterialButton
    private lateinit var exitBtn: MaterialButton

    private var mRewardedAd: RewardedAd? = null

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

        loadRewardAd()

        showAdBtn=findViewById(R.id.showAdBtn)
        exitBtn=findViewById(R.id.ExitBtn)

        showAdBtn.setOnClickListener(){

        }

        exitBtn.setOnClickListener(){//Hacer que salga de la app

        }
    }

    private fun loadRewardAd(){


        RewardedAd.load(
            this,
            getString(R.string.rewarded_ad_id_live),
            AdRequest.Builder().build(), 
            object: RewardedAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                        Log.d(TAG,"A fallado")
                    mRewardedAd=null
                }

                override fun onAdLoaded(RewardedAd: RewardedAd) {
                    super.onAdLoaded(RewardedAd)

                    Log.d(TAG,"Hay anuncio")
                    mRewardedAd= RewardedAd
                }
            }
        )
    }


    private fun showRewaredAd(){
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
                    loadRewardAd()
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
                Toast.makeText(this,"REWARD EARNED!!!!", Toast.LENGTH_SHORT).show()

            }

        }
        else{
            Toast.makeText(this,"No se ha cargado la vaina de anuncio", Toast.LENGTH_SHORT).show()
        }
    }




}