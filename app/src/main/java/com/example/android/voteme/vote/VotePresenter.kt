package com.example.android.voteme.vote

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
}