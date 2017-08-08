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
                mVotesRepository.isVoted(id, object : DataSource.IsVotedCallback {
                    override fun onResult(isVoted: Boolean) {
                        mView.showVote(vote,isVoted)
                        mVotesRepository.addChildListener(id,object : DataSource.RefreshVoteCallback{
                            override fun onVoteUpdated(varinat: String, newCount: Int) {
                                mView.updateVote(varinat,newCount)
                            }
                        })
                    }
                })

            }

        })
    }

    override fun chooseVariant(voteId: String, variant: String) {
        mView.showProgressBar()
        mVotesRepository.makeElect(voteId,variant,object : DataSource.ElectCallback{
            override fun onElected() {
                mView.hideProgressBar()
                Log.d("FIREBASE","VARIANT ELECTED")
            }

            override fun onFailure(exception: Exception) {
                mView.hideProgressBar()
            }
        })
    }

    override fun removeChildListener(id: String) {
        mVotesRepository.removeChilEventListener(id)
    }

    override fun joinToVote(id: String) {
        mVotesRepository.joinToVote(id)
    }
}