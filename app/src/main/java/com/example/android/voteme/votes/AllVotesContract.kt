package com.example.android.voteme.votes

import com.example.android.voteme.base.BasePresenter
import com.example.android.voteme.base.BaseView
import com.example.android.voteme.model.Vote

/**
 * Created by Valentin on 16.07.2017.
 */
interface AllVotesContract {
    interface View : BaseView<Presenter>{
        fun showVotes(votes:ArrayList<Vote>)

        fun showError(msg:String?)

        fun showAddedVote(vote: Vote)

        fun removeVote(id:String)

        fun hideLoadingPanel()

        fun hideEmptyVotesPanel()

        fun showEmptyVotesPanel()
    }

    interface Presenter : BasePresenter<View>{
        fun loadVotes()
    }
}