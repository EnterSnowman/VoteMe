package com.example.android.voteme.addvote

import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.VotesRepository
import com.example.android.voteme.utils.Constants
import java.lang.Exception

/**
 * Created by Valentin on 17.07.2017.
 */
class AddVotePresenter(override var mView: AddVoteContract.View) : AddVoteContract.Presenter {

    var mVotesRepository : VotesRepository

    init {
        mVotesRepository = VotesRepository.getInstance()
        mView.setPresenter(this)
    }
    override fun addVote(title: String,variants: ArrayList<String>) {
        if (title.equals(""))
            mView.showError(Constants.EMPTY_TITLE)
        else {
            if (variants.size<2)
                mView.showError(Constants.NOT_FULL_LIST)
            else{
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
        }
    }

    override fun deleteVariant(positon: Int) {
        mView.deleteVarinat(positon)
    }

    override fun addVarinat(variant: String) {
        if (!mView.isVariantExists(variant))
        mView.showAddedVariant(variant)
        else{
            mView.showError(Constants.NON_UNIQUE_VARIANT)
        }
    }

}