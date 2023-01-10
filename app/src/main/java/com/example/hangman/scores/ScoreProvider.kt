package com.example.hangman.scores

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ScoreProvider {


    companion object{
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Players")
        var value: Map<*, *>? = null

        public fun GetData(){

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                     value = dataSnapshot.value as Map<*, *>? //Recoge datos
                    Log.d("DATA", "Value is: " + value)

                    for(key in value?.keys!!) {
                        Log.d("DATA", "Recorre Hashmap")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    Log.w("DATA", "Failed to read value.", error.toException())
                }
            })

        }

        var scoreListDef = listOf(
            ScoreList(
                "ivansales2@gmail.com",
                1000
            ),
            ScoreList(
                "josepromera@gmail.com",
                500
            ),
            ScoreList(
                "pabloperpinan@gmail.com",
                300
            )
        )
    }







}
