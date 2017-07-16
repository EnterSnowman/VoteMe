package com.example.android.voteme.loginregistration

import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.UserRepository
import java.lang.Exception

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
                view.goToVotesActivity()
            }

            override fun onSignInFailure(exception: Exception?) {
                view.makeToast("Login failure")
                view.hideLoading()
            }

        })
    }

    override fun signUp(email: String, password: String) {
        mUserRepository?.signUp(email,password,object : DataSource.SignUpCallback{
            override fun onSignUpCompleted() {
                view.makeToast("New user registered")
                view.hideLoading()
                view.goToVotesActivity()
            }

            override fun onSignUpFailure(exception: Exception?) {
                view.makeToast("Sign up failed")
                view.hideLoading()
            }
        })
    }

    override fun autoLogin() {
        if (mUserRepository?.getCurrentUser()!= null) view.goToVotesActivity()
    }



}