package com.example.android.voteme.votes

import com.example.android.voteme.base.BasePresenter
import com.example.android.voteme.base.BaseView
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.model.Vote

/**
 * Created by Valentin on 16.07.2017.
 */
interface MyVotesContract {
    interface View : BaseView<Presenter> {
        fun showVotes(votes:ArrayList<Vote>)

        fun showError(msg:String?)

        fun showAddedVote(vote: Vote)

        fun hideLoadingPanel()

        fun showEmptyVotesPanel()

        fun hideEmptyVotesPanel()
        fun removeVote(id: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadVotes()

    }
}