package com.example.android.voteme.vote

import android.util.Log
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.VotesRepository
import com.example.android.voteme.model.Vote
import java.lang.Exception

/**
 * Created by Valentin on 28.07.2017.
 */
class VotePresenter(override var mView: VoteContract.View) : VoteContract.Presenter {


    var mVotesRepository : VotesRepository

    init {
        mView.setPresenter(this)
        mVotesRepository = VotesRepository.getInstance()
    }

    override fun loadVote(id: String) {
        mVotesRepository.getVoteById(id,object : DataSource.SingleVoteLoadCallback{
            override fun onFailure(exception: Exception) {

            }

            override fun onLoad(vote: Vote) {
                mView.showVote(vote)
            }

        })
    }

    override fun chooseVariant(voteId: String, variant: String) {
        mVotesRepository.makeElect(voteId,variant,object : DataSource.ElectCallback{
            override fun onElected() {
                Log.d("FIREBASE","VARIANT ELECTED")
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }
}