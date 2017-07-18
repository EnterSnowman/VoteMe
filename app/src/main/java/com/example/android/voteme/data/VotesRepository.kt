package com.example.android.voteme.data

import com.example.android.voteme.utils.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Valentin on 16.07.2017.
 */
class VotesRepository private constructor(){
    var mDatabase : DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.VOTES)
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
}