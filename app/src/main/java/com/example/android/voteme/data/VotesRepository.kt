package com.example.android.voteme.data

import android.util.Log
import com.example.android.voteme.model.Vote
import com.example.android.voteme.utils.Constants
import com.example.android.voteme.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception
import kotlin.concurrent.fixedRateTimer

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
    var mUserDatabaseJoined : DatabaseReference = FirebaseDatabase.getInstance()
            .getReference(Constants.USERS)
            .child(FirebaseAuth.getInstance().currentUser?.uid)
            .child(Constants.JOINED)
    var mChildEventListener : ChildEventListener? = null
    var mChildEventListenerCreated : ArrayList<ChildEventListener>?
    var mChildEventListenerJoined : ChildEventListener? = null
    init {
        mChildEventListenerCreated = ArrayList<ChildEventListener>()
    }
    companion object {
        private var repository : VotesRepository? = null
        fun getInstance(): VotesRepository {
            if (repository==null)
                repository = VotesRepository()
            return repository!!

        }
    }

    fun addVote(title:String,variants: ArrayList<String>,isOpen:Boolean,isRevotable:Boolean,callback:DataSource.VoteAddedCallback){
        var id =  mDatabase.push().key
        var vote = HashMap<String,Any>()
        vote.put(Constants.TITLE,title)

        var vars = HashMap<String,Int>()
        var newVariants = Utils.addKeySuffix(variants)
        for (v in newVariants)
            vars.put(v,0)
        vote.put(Constants.VARIANTS,vars)
        vote.put(Constants.IS_OPEN,isOpen)
        vote.put(Constants.IS_REVOTABLE,isRevotable)
        mDatabase.child(id).setValue(vote).addOnCompleteListener{task -> if (task.isSuccessful) {
            callback.onComplete()
            mUserDatabase.child(id).setValue(Constants.CREATED)
        }
        else callback.onFailure(task.exception) }

    }

    fun getVotesJoinedByUser(callback: DataSource.VotesCallback){
        mUserDatabaseJoined.addListenerForSingleValueEvent(object : ValueEventListener{
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

    fun getVotesCreatedByUser(callback: DataSource.VotesCallback){
        mUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener{
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
                Log.d("FIREBASE",p0?.key)
                var vote = p0?.getValue(Vote::class.java)!!
                vote.variants = Utils.removeKeySuffix(vote.variants) as HashMap<String, Int>
                vote.id = p0.key
                vote.isOpen = p0.child(Constants.IS_OPEN)?.getValue(Boolean::class.java)!!
                vote.isRevotable = p0.child(Constants.IS_REVOTABLE)?.getValue(Boolean::class.java)!!
                callback.onLoad(vote)
            }

        })
    }



    fun isVoted(id:String,callback: DataSource.IsVotedCallback){
        mUserDatabaseVoted.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    if (p0.exists()) callback.onResult(true) else callback.onResult(false)

                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }

    fun makeElect(id:String,variant:String,callback:DataSource.ElectCallback){
        mUserDatabaseVoted.child(id).setValue(variant.plus(Constants.KEY)).addOnCompleteListener{task ->
            if(task.isSuccessful)
                callback.onElected()
            else
                callback.onFailure(task.exception!!)
        }
    }

    fun addChildListener(id:String,callback: DataSource.RefreshVoteCallback){
        mChildEventListener =  mDatabase.child(id).child(Constants.VARIANTS).addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                if (p0 != null) {
                    Log.d("FIREBASE LOG","Child updated {$id} ")
                    var c = p0.value as Long
                    callback.onVoteUpdated(p0.key.substring(0,p0.key.length-4), c.toInt())
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })
    }

    fun isEmptyCreatedVotes(callback: DataSource.NodeExistingCallback){
        mUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    Log.d("FIREBASE LOG","isEmptyCreatedVotes ${p0.exists()} ")
                    callback.onExist(p0.exists())
                }
            }
        })
    }

    fun isEmptyJoinedVotes(callback: DataSource.NodeExistingCallback){
        mUserDatabaseJoined.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    Log.d("FIREBASE LOG","isEmptyJoinedVotes ${p0.exists()} ")
                    callback.onExist(p0.exists())
                }
            }
        })
    }

    fun addChildEventListenerCreated(callback: DataSource.ListRefreshCallback){
        Log.d("FIREBASE","addChildEventListenerCreated ok")
        mChildEventListenerCreated!!.add(mUserDatabase.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                Log.d("Child List Created","onChildAdded")
                getVoteById(p0!!.key,object : DataSource.SingleVoteLoadCallback {
                    override fun onFailure(exception: Exception) {
                        Log.d("FIREBASE_debug","${exception?.message} \n ${exception?.localizedMessage}")
                    }

                    override fun onLoad(vote:Vote) {
                        callback.onVoteAdded(vote)
                    }

                })
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        }))
    }

    fun addChildEventListenerJoined(callback: DataSource.ListRefreshCallback){
        Log.d("FIREBASE","addChildEventListenerJoined ok")
        mChildEventListenerJoined = mUserDatabaseJoined.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                Log.d("Child List Joined","onChildAdded")
                getVoteById(p0!!.key,object : DataSource.SingleVoteLoadCallback {
                    override fun onFailure(exception: Exception) {

                    }

                    override fun onLoad(vote:Vote) {
                        callback.onVoteAdded(vote)
                    }

                })
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })
    }

    fun removeChildEventListenersCreated(){
        if (mChildEventListenerCreated!!.size>0)
            for (l in mChildEventListenerCreated!!)
                mDatabase.removeEventListener(l)
    }

    fun removeChildEventListenerJoined(){
        if (mChildEventListenerJoined!=null)
            mUserDatabaseJoined.removeEventListener(mChildEventListenerJoined)
    }

    fun removeChilEventListener(id:String){
        if (mChildEventListener!=null)
            mDatabase.child(id).child(Constants.VARIANTS).removeEventListener(mChildEventListener)
    }

    fun joinToVote(id:String){
        mUserDatabase.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.exists()){
                    mUserDatabaseJoined.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) {            }

                        override fun onDataChange(p0: DataSnapshot?) {
                            if (!p0!!.exists()){
                                p0.ref.setValue(Constants.JOINED)
                            }
                        }
                    })
                }
            }
        })

    }


}