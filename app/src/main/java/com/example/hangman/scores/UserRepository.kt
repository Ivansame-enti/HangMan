package com.example.hangman.scores

import androidx.lifecycle.MutableLiveData
import com.example.hangman.gameActivity.User
import com.google.firebase.database.*

class UserRepository {

    private  val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Players")
    public var _userList : List<User>?=null
    @Volatile private var INSTANCE : UserRepository?= null

    fun getInstance(): UserRepository{
    return INSTANCE?:synchronized(this){

            val instance = UserRepository()
        INSTANCE=instance
        instance
        }

    }

    fun loadUsers(userList: MutableLiveData<List<User>>){

        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try{
                             _userList = snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(User::class.java)!!

                        }

                    userList.postValue(_userList)

                }catch ( e: Exception){

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}