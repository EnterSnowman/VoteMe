package com.example.android.voteme.addvote

import com.example.android.voteme.base.BasePresenter
import com.example.android.voteme.base.BaseView

/**
 * Created by Valentin on 16.07.2017.
 */
interface AddVoteContract {
    interface View : BaseView<Presenter>{
        fun showAddedVariant(variant: String)

        fun deleteVarinat(positon: Int)

        fun addVote()

        fun showLoading()

        fun hideLoading()

        fun isVariantExists(variant: String) : Boolean

        fun showError(type : Int)

        fun finish()
    }

    interface Presenter : BasePresenter<View>{
        fun addVarinat(variant : String)

        fun deleteVariant(positon : Int)

        fun addVote(title: String,variants : ArrayList<String>,isOpen:Boolean,isRevotable:Boolean)
    }
}