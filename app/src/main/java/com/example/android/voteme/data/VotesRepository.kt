package com.example.android.voteme.data

import android.util.Log
import com.example.android.voteme.model.Vote
import com.example.android.voteme.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by Valentin on 16.07.2017.
 */
class VotesRepository private constructor(){
    var mDatabase : DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.VOTES)
    var mUserDatabase : DatabaseReference = FirebaseDatabase.getInstance()
            .getReference(Constants.USERS)
            .child(FirebaseAuth.getInstance().currentUser?.uid)
            .child(Constants.CREATED)
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
                callback.onLoad(p0?.getValue(Vote::class.java)!!)
            }

        })
    }
}