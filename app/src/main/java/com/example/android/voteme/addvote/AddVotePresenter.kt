package com.example.android.voteme.addvote

import android.util.Log
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
    override fun addVote(title: String,variants: ArrayList<String>,isOpen:Boolean,isRevotable:Boolean) {
        if (title.equals(""))
            mView.showError(Constants.EMPTY_TITLE)
        else {
            if (variants.size<2)
                mView.showError(Constants.NOT_FULL_LIST)
            else{
                mView.showLoading()
                //var vars = variants.map {it.plus(Constants.KEY)} as ArrayList
                mVotesRepository.createVote(title,variants,isOpen,isRevotable,object : DataSource.VoteAddedCallback{
                    override fun onComplete() {
                        mView.hideLoading()
                        mView.finish()
                    }

                    override fun onFailure(s:String) {
                        Log.d("Add new Vote",s)
                        mView.showConnectivityError(s)
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