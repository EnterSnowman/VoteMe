package com.example.android.voteme.addvote

import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.VotesRepository
import java.lang.Exception

/**
 * Created by Valentin on 17.07.2017.
 */
class AddVotePresenter(override var mView: AddVoteContract.View) : AddVoteContract.Presenter {

    var mVotesRepository : VotesRepository

    init {
        mVotesRepository = VotesRepository.getInstance()
    }
    override fun addVote(title: String,variants: ArrayList<String>) {
        mView.showLoading()
        mVotesRepository.addVote(title,variants,object : DataSource.VoteAddedCallback{
            override fun onComplete() {
                mView.hideLoading()
            }

            override fun onFailure(exception: Exception?) {
                mView.hideLoading()
            }
        })
    }

    override fun deleteVariant(positon: Int) {
        mView.deleteVarinat(positon)
    }

    override fun addVarinat(variant: String) {
        mView.showAddedVariant(variant)
    }

    init {
        mView.setPresenter(this)
    }
}