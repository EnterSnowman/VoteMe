package com.example.android.voteme.addvote

/**
 * Created by Valentin on 17.07.2017.
 */
class AddVotePresenter(override var mView: AddVoteContract.View) : AddVoteContract.Presenter {
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