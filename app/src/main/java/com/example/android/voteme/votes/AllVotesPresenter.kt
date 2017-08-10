package com.example.android.voteme.votes

import android.util.Log
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.VotesRepository
import com.example.android.voteme.model.Vote
import java.lang.Exception

/**
 * Created by Valentin on 16.07.2017.
 */
class AllVotesPresenter(override var mView: AllVotesContract.View) : AllVotesContract.Presenter {

    var mVotesRepository : VotesRepository

    init {
        mVotesRepository = VotesRepository.getInstance()
        Log.d("AllVotes", mVotesRepository.toString())
        mView.setPresenter(this)
    }
    override fun loadVotes() {
        mVotesRepository.getVotesCreatedByUser(object : DataSource.VotesCallback{
            override fun onComplete(votes: ArrayList<Vote>?) {
                if (votes != null) {
                    mView.showVotes(votes)

                }
                mVotesRepository.addChildEventListenerCreated(object : DataSource.ListRefreshCallback {
                    override fun onVoteAdded(newVote: Vote) {
                        mView.showAddedVote(newVote)
                    }
                })
                mVotesRepository.addChildEventListenerJoined(object : DataSource.ListRefreshCallback {
                    override fun onVoteAdded(newVote: Vote) {
                        mView.showAddedVote(newVote)
                    }
                })
                mView.showError("Loading completed created")
            }

            override fun onFailure(exception: Exception?) {
                mView.showError(exception?.message)
            }
        })

        mVotesRepository.getVotesJoinedByUser(object : DataSource.VotesCallback{
            override fun onComplete(votes: ArrayList<Vote>?) {
                if (votes != null) {
                    mView.showVotes(votes)
                }
                mView.showError("Loading completed joined")
            }

            override fun onFailure(exception: Exception?) {
                mView.showError(exception?.message)
            }
        })
    }
}