package com.example.android.voteme.loginregistration

import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.UserRepository

/**
 * Created by Valentin on 15.07.2017.
 */
class LoginRegistrationPresenter(override var view: LoginRegistrationContract.View) : LoginRegistrationContract.Presenter{
    var mUserRepository : UserRepository? = null
    constructor(view: LoginRegistrationContract.View, userRepository: UserRepository) : this(view){
        view.setPresenter(this)
        mUserRepository = userRepository;
    }
    override fun signIn(email: String, password: String) {
        mUserRepository?.signIn(email,password,object : DataSource.SignInCallback{
            override fun onSignInCompleted() {
                view.makeToast("Login completed")
                view.hideLoading()
            }

            override fun onSignInFailure() {
                view.makeToast("Login failure")
                view.hideLoading()
            }

        })
    }

    override fun signUp(email: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}