package com.example.android.voteme.loginregistration

import android.util.Log
import com.example.android.voteme.data.DataSource
import com.example.android.voteme.data.UserRepository
import com.example.android.voteme.data.VotesRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import java.lang.Exception

/**
 * Created by Valentin on 15.07.2017.
 */
class LoginRegistrationPresenter(override var mView: LoginRegistrationContract.View) : LoginRegistrationContract.Presenter{


    var mUserRepository : UserRepository? = null
    constructor(view: LoginRegistrationContract.View, userRepository: UserRepository) : this(view){
        view.setPresenter(this)
        mUserRepository = userRepository;
    }
    override fun signIn(email: String, password: String) {
        mUserRepository?.signIn(email,password,object : DataSource.SignInCallback{


            override fun onSignInCompleted() {
                mView.makeToast("Login completed")
                mView.hideLoading()
                mView.goToVotesActivity()
                VotesRepository.getInstance().reinitDatabaseReferences()
            }

            override fun onSignInFailure(exception: Exception?) {
                handleException(exception!!)
                mView.makeToast("Login failure")
                mView.hideLoading()
            }

        })
    }

    fun handleException(exception: Exception){
        var errorCode = ""
        Log.d("Exception",exception.message)
        when(exception){
            is FirebaseNetworkException ->{
                Log.d("Network",exception.message)
                errorCode = "NETWORK_ERROR"
            }
            is FirebaseAuthException ->{
                Log.d("Login",exception .errorCode)
                Log.d("Login",exception.message)
                errorCode = exception.errorCode
            }
        }
        mView.showError(errorCode)
    }

    override fun signUp(email: String, password: String) {
        mUserRepository?.signUp(email,password,object : DataSource.SignUpCallback{
            override fun onSignUpCompleted() {
                mUserRepository?.sendVerificationEmail(object : DataSource.EmailVerificationCallback{
                    override fun onSended() {
                        mView.makeToast("New user registered")
                        mView.hideLoading()
                        mView.showEmailVerificationMessage()
                        VotesRepository.getInstance().reinitDatabaseReferences()
                        //mView.goToVotesActivity()
                    }
                    override fun onSendedFailure(exception: Exception?) {
                        handleException(exception!!)
                    }
                })

            }

            override fun onSignUpFailure(exception: Exception?) {
                handleException(exception!!)
                mView.makeToast("Sign up failed")
                mView.hideLoading()
            }
        })
    }

    override fun autoLogin() {
        if (mUserRepository?.getCurrentUser()!= null) mView.goToVotesActivity()
    }

    override fun sendRestorePasswordEmail(email: String) {
        UserRepository.getInstance().mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task -> if (task.isSuccessful) mView.makeToast("Restoration email was sended") else mView.makeToast("Error") }
    }



}