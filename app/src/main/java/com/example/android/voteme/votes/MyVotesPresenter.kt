package com.example.android.voteme.votes

import android.util.Log
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.VotesRepository
import com.example.android.voteme.model.Vote
import java.lang.Exception

/**
 * Created by Valentin on 16.07.2017.
 */
class MyVotesPresenter(override var mView: MyVotesContract.View) : MyVotesContract.Presenter {
    var mVotesRepository : VotesRepository

    init {
        mVotesRepository = VotesRepository.getInstance()
        Log.d("MyVotes", mVotesRepository.toString())
        mView.setPresenter(this)
    }
    override fun loadVotes() {
        /*mVotesRepository.getVotesCreatedByUser(object : DataSource.VotesCallback{
            override fun onComplete(votes: ArrayList<Vote>?) {
                if (votes != null) {
                    mView.showVotes(votes)
                }

                mView.showError("Loading completed")
            }

            override fun onFailure(exception: Exception?) {
                    mView.showError(exception?.message)
            }
        })*/
        mVotesRepository.isEmptyCreatedVotes(object : DataSource.NodeExistingCallback{
            override fun onExist(exists: Boolean) {
                if (!exists){
                    mView.showEmptyVotesPanel()
                    mView.hideLoadingPanel()
                }
            }
        })

        mVotesRepository.addChildEventListenerCreated(object : DataSource.ListRefreshCallback {
            override fun onVoteAdded(newVote: Vote) {
                mView.showAddedVote(newVote)
                mView.hideLoadingPanel()
                mView.hideEmptyVotesPanel()
            }
            override fun onVoteReoved(id: String) {
                mView?.removeVote(id)
            }
        })
    }
}