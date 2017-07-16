package com.example.android.voteme.votes

import com.example.android.voteme.base.BasePresenter
import com.example.android.voteme.base.BaseView

/**
 * Created by Valentin on 16.07.2017.
 */
interface AllVotesContract {
    interface View : BaseView<Presenter>{

    }

    interface Presenter : BasePresenter<View>{

    }
}