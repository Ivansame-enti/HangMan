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
        var keyList: ArrayList<Any?>? = null
        var valueList: ArrayList<Any?>? = null
        var numero: Int = 0
        var scoreListDef = listOf(
            ScoreList(
                keyList.toString(),
                "1000"
            ),
            )

        fun GetData(){
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                   scoreListDef = scoreListDef.drop(scoreListDef.size)
                     value = dataSnapshot.value as Map<*, *>? //Recoge datos
                    Log.d("DATA", "Value is: " + value)

                    for(key in value?.keys!!) {
                        Log.d("DATA", "Recorre Hashmap")
                         keyList = ArrayList(value?.keys)
                         valueList = ArrayList(value?.values)

                        val hashMap = valueList!!.elementAt(numero) as HashMap<String,String>

                        scoreListDef += ScoreList(
                            keyList?.elementAt(numero).toString(),
                            hashMap.get("score").toString()
                        )
                        numero=numero+1
                    }
                    numero=0
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("DATA", "Failed to read value.", error.toException())
                }
            })
        }
    }
}
