package com.example.android.voteme.loginregistration

import com.example.android.voteme.base.BasePresenter
import com.example.android.voteme.base.BaseView

/**
 * Created by Valentin on 15.07.2017.
 */
interface LoginRegistrationContract {
    interface View : BaseView<Presenter>{
        fun makeToast(msg: String)

        fun showLoading(msg : String)

        fun hideLoading()

        fun showError(errorCode:String)

        fun goToVotesActivity()

        fun showEmailVerificationMessage()
    }
    interface Presenter : BasePresenter<View>{
        fun signIn(email: String,password : String)

        fun signUp(email: String,password : String)

        fun autoLogin()

        fun sendRestorePasswordEmail(email:String)
    }
}