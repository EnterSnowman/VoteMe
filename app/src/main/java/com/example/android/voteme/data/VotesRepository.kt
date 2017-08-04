package com.example.android.voteme.data

import android.util.Log
import com.example.android.voteme.model.Vote
import com.example.android.voteme.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

/**
 * Created by Valentin on 16.07.2017.
 */
class VotesRepository private constructor(){
    var mDatabase : DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.VOTES)
    var mUserDatabase : DatabaseReference = FirebaseDatabase.getInstance()
            .getReference(Constants.USERS)
            .child(FirebaseAuth.getInstance().currentUser?.uid)
            .child(Constants.CREATED)
    var mUserDatabaseVoted : DatabaseReference = FirebaseDatabase.getInstance()
            .getReference(Constants.USERS)
            .child(FirebaseAuth.getInstance().currentUser?.uid)
            .child(Constants.VOTED)
    companion object {
        private var repository : VotesRepository? = null
        fun getInstance(): VotesRepository {
            if (repository==null)
                repository = VotesRepository()
            return repository!!

        }
    }

    fun addVote(title:String,variants: ArrayList<String>,callback:DataSource.VoteAddedCallback){
        var id =  mDatabase.push().key
        var vote = HashMap<String,Any>()
        vote.put(Constants.TITLE,title)
        var vars = HashMap<String,Int>()
        for (v in variants)
            vars.put(v,0)
        vote.put(Constants.VARIANTS,vars)
        mDatabase.child(id).setValue(vote).addOnCompleteListener{task -> if (task.isSuccessful) callback.onComplete() else callback.onFailure(task.exception) }

    }

    fun getVotesCreatedByUser(callback: DataSource.VotesCallback){
        mUserDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                callback.onFailure(p0?.toException())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                var votes  = ArrayList<Vote>()
                var count : Long = 0
                var size  = p0?.childrenCount
                p0?.children?.forEach {
                    getVoteById(it.key,object : DataSource.SingleVoteLoadCallback{
                        override fun onFailure(exception: Exception) {

                        }

                        override fun onLoad(vote: Vote) {
                            count++
                            votes.add(vote)
                            Log.d("FIRE",vote.title)
                            if (count==size){
                                Log.d("FIRE","Loading votes completed")
                                callback.onComplete(votes)
                            }
                        }
                    })
                }

            }
        })
    }

    fun getVoteById(id : String,callback: DataSource.SingleVoteLoadCallback){
        mDatabase.child(id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }
            override fun onDataChange(p0: DataSnapshot?) {
                var vote = p0?.getValue(Vote::class.java)!!
                vote.id = p0.key
                callback.onLoad(vote)
            }

        })
    }

    fun makeElect(id:String,variant:String,callback:DataSource.ElectCallback){
        mUserDatabaseVoted.child(id).setValue(variant).addOnCompleteListener{task ->
            if(task.isSuccessful)
                callback.onElected()
            else
                callback.onFailure(task.exception!!)
        }
    }

    fun addChildListener(id:String,callback: DataSource.RefreshVoteCallback){
        mDatabase.child(id).child(Constants.VARIANTS).addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                if (p0 != null) {
                    Log.d("FIREBASE LOG","Child updated {$id} ")
                    var c = p0.value as Long
                    callback.onVoteUpdated(p0.key, c.toInt())
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })
    }
}