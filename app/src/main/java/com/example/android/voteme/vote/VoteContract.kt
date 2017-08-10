package com.example.android.voteme.vote

import com.example.android.voteme.base.BasePresenter
import com.example.android.voteme.base.BaseView
import com.example.android.voteme.model.Vote

/**
 * Created by Valentin on 28.07.2017.
 */
interface VoteContract {
    interface View : BaseView<Presenter>{
        fun showVote(vote: Vote,isVoted:Boolean)

        fun updateVote(variant : String,newCount : Int)

        fun showProgressBar()

        fun hideProgressBar()

        fun hideVotingPanel()
    }

    interface Presenter : BasePresenter<View>{
        fun loadVote(id: String)

        fun chooseVariant(voteId: String,variant:String)

        fun removeChildListener(id: String)

        fun joinToVote(id:String)
    }


}